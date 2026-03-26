package com.beangrabber.data.model

/**
 * Represents a Beans Bag reward link
 */
data class BeansBag(
    val id: String,
    val link: String,
    val messageId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val isClicked: Boolean = false,
    val clickedAt: Long? = null
) {
    /**
     * Mark this beans bag as clicked
     */
    fun markAsClicked(): BeansBag {
        return copy(isClicked = true, clickedAt = System.currentTimeMillis())
    }

    /**
     * Check if this beans bag is expired (older than 5 minutes)
     */
    fun isExpired(): Boolean {
        val fiveMinutes = 5 * 60 * 1000
        return System.currentTimeMillis() - timestamp > fiveMinutes
    }
}
