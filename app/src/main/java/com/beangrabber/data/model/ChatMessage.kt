package com.beangrabber.data.model

import java.util.Date

/**
 * Represents a chat message in the system
 */
data class ChatMessage(
    val id: String,
    val userId: String,
    val username: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val containsBeansBag: Boolean = false,
    val beansBagLinks: List<String> = emptyList(),
    val isHighlighted: Boolean = false
) {
    /**
     * Check if this message contains reward links
     */
    fun hasRewardLinks(): Boolean = containsBeansBag && beansBagLinks.isNotEmpty()

    /**
     * Get formatted timestamp
     */
    fun getFormattedTime(): String {
        val date = Date(timestamp)
        val hours = date.hours.toString().padStart(2, '0')
        val minutes = date.minutes.toString().padStart(2, '0')
        return "$hours:$minutes"
    }
}
