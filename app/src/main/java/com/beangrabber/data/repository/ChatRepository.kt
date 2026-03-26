package com.beangrabber.data.repository

import com.beangrabber.data.model.BeansBag
import com.beangrabber.data.model.ChatMessage
import com.beangrabber.util.LinkDetector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Repository for managing chat messages and beans bags
 * This simulates a real-time chat system
 */
class ChatRepository {

    // Maximum messages to keep in memory for performance
    private val MAX_MESSAGES = 100

    // Chat messages state
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Active beans bags state
    private val _activeBeansBags = MutableStateFlow<List<BeansBag>>(emptyList())
    val activeBeansBags: StateFlow<List<BeansBag>> = _activeBeansBags.asStateFlow()

    // All beans bags (including clicked ones)
    private val _allBeansBags = MutableStateFlow<List<BeansBag>>(emptyList())
    val allBeansBags: StateFlow<List<BeansBag>> = _allBeansBags.asStateFlow()

    /**
     * Add a new message to the chat
     */
    fun addMessage(userId: String, username: String, messageText: String) {
        val message = LinkDetector.parseChatMessage(userId, username, messageText)

        // Add message
        val currentMessages = _messages.value.toMutableList()
        currentMessages.add(message)

        // Keep only last MAX_MESSAGES
        if (currentMessages.size > MAX_MESSAGES) {
            currentMessages.removeAt(0)
        }

        _messages.value = currentMessages

        // Extract and add beans bags if present
        if (message.hasRewardLinks()) {
            val newBeansBags = LinkDetector.extractBeansBags(message)
            addBeansBags(newBeansBags)
        }
    }

    /**
     * Add beans bags to the collection
     */
    private fun addBeansBags(newBeansBags: List<BeansBag>) {
        val currentBags = _allBeansBags.value.toMutableList()
        currentBags.addAll(newBeansBags)

        // Deduplicate
        val deduped = LinkDetector.deduplicateBeansBags(currentBags)
        _allBeansBags.value = deduped

        // Update active beans bags
        updateActiveBeansBags()
    }

    /**
     * Update active beans bags (not clicked, not expired)
     */
    private fun updateActiveBeansBags() {
        val active = LinkDetector.filterActiveBeansBags(_allBeansBags.value)
        _activeBeansBags.value = active
    }

    /**
     * Mark a beans bag as clicked
     */
    fun markBeansBagClicked(beansBagId: String) {
        val allBags = _allBeansBags.value.toMutableList()
        val index = allBags.indexOfFirst { it.id == beansBagId }

        if (index != -1) {
            allBags[index] = allBags[index].markAsClicked()
            _allBeansBags.value = allBags
            updateActiveBeansBags()
        }
    }

    /**
     * Get messages containing reward links only
     */
    fun getRewardMessages(): List<ChatMessage> {
        return _messages.value.filter { it.hasRewardLinks() }
    }

    /**
     * Clear old messages
     */
    fun clearOldMessages() {
        val recentMessages = _messages.value.takeLast(50)
        _messages.value = recentMessages
    }

    /**
     * Clear all messages
     */
    fun clearAllMessages() {
        _messages.value = emptyList()
        _allBeansBags.value = emptyList()
        _activeBeansBags.value = emptyList()
    }

    /**
     * Simulate receiving messages for testing
     */
    fun simulateMessages() {
        val sampleMessages = listOf(
            Triple("user1", "Alice", "Hey everyone! Check out this Beans bag https://example.com/bag1 Click for more info >"),
            Triple("user2", "Bob", "Hello, how's everyone doing?"),
            Triple("user3", "Charlie", "New beans bag available! https://example.com/bag2 Grab it now!"),
            Triple("user4", "Diana", "Anyone got the latest beans bag?"),
            Triple("user5", "Eve", "Beans bag alert: https://example.com/bag3 Click for more info >"),
            Triple("user6", "Frank", "Great stream today!"),
            Triple("user7", "Grace", "Don't miss this reward link https://example.com/bag4"),
        )

        sampleMessages.forEach { (userId, username, message) ->
            addMessage(userId, username, message)
        }
    }
}
