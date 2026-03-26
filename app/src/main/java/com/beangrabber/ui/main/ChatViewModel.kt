package com.beangrabber.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.beangrabber.data.model.BeansBag
import com.beangrabber.data.model.ChatMessage
import com.beangrabber.data.model.GrabStatistics
import com.beangrabber.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing chat and beans bag functionality
 */
class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository()

    // Filter state
    private val _showOnlyRewards = MutableStateFlow(false)
    val showOnlyRewards: StateFlow<Boolean> = _showOnlyRewards.asStateFlow()

    // Statistics
    private val _statistics = MutableStateFlow(GrabStatistics())
    val statistics: StateFlow<GrabStatistics> = _statistics.asStateFlow()

    // New beans bag notification
    private val _newBeansBagNotification = MutableStateFlow<BeansBag?>(null)
    val newBeansBagNotification: StateFlow<BeansBag?> = _newBeansBagNotification.asStateFlow()

    // Auto-click mode
    private val _autoClickEnabled = MutableStateFlow(false)
    val autoClickEnabled: StateFlow<Boolean> = _autoClickEnabled.asStateFlow()

    private val _autoClickDelay = MutableStateFlow(1000L) // milliseconds
    val autoClickDelay: StateFlow<Long> = _autoClickDelay.asStateFlow()

    init {
        observeActiveBeansBags()
    }

    /**
     * Get chat messages (filtered or all)
     */
    fun getMessages(): StateFlow<List<ChatMessage>> {
        return if (_showOnlyRewards.value) {
            MutableStateFlow(chatRepository.getRewardMessages()).asStateFlow()
        } else {
            chatRepository.messages
        }
    }

    /**
     * Get all messages (unfiltered)
     */
    fun getAllMessages(): StateFlow<List<ChatMessage>> {
        return chatRepository.messages
    }

    /**
     * Get active beans bags
     */
    fun getActiveBeansBags(): StateFlow<List<BeansBag>> {
        return chatRepository.activeBeansBags
    }

    /**
     * Toggle reward filter
     */
    fun toggleRewardFilter() {
        _showOnlyRewards.value = !_showOnlyRewards.value
    }

    /**
     * Set reward filter
     */
    fun setRewardFilter(enabled: Boolean) {
        _showOnlyRewards.value = enabled
    }

    /**
     * Add a new message
     */
    fun addMessage(userId: String, username: String, message: String) {
        viewModelScope.launch {
            chatRepository.addMessage(userId, username, message)
        }
    }

    /**
     * Mark beans bag as clicked/grabbed
     */
    fun grabBeansBag(beansBagId: String) {
        viewModelScope.launch {
            chatRepository.markBeansBagClicked(beansBagId)

            // Update statistics
            val currentStats = _statistics.value
            _statistics.value = currentStats.incrementSuccess()
        }
    }

    /**
     * Clear notification
     */
    fun clearNotification() {
        _newBeansBagNotification.value = null
    }

    /**
     * Observe active beans bags for notifications
     */
    private fun observeActiveBeansBags() {
        viewModelScope.launch {
            var previousBags: List<BeansBag> = emptyList()

            chatRepository.activeBeansBags.collect { activeBags ->
                // Check for new beans bags
                val newBags = activeBags.filter { bag ->
                    previousBags.none { it.id == bag.id }
                }

                // Show notification for newest bag
                if (newBags.isNotEmpty()) {
                    _newBeansBagNotification.value = newBags.maxByOrNull { it.timestamp }
                }

                previousBags = activeBags
            }
        }
    }

    /**
     * Toggle auto-click mode
     */
    fun toggleAutoClick() {
        _autoClickEnabled.value = !_autoClickEnabled.value
    }

    /**
     * Set auto-click delay
     */
    fun setAutoClickDelay(delayMs: Long) {
        _autoClickDelay.value = delayMs
    }

    /**
     * Clear old messages for performance
     */
    fun clearOldMessages() {
        viewModelScope.launch {
            chatRepository.clearOldMessages()
        }
    }

    /**
     * Load sample data for testing
     */
    fun loadSampleData() {
        viewModelScope.launch {
            chatRepository.simulateMessages()
        }
    }
}
