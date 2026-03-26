package com.beangrabber.util

import com.beangrabber.data.model.BeansBag
import com.beangrabber.data.model.ChatMessage
import java.util.UUID
import java.util.regex.Pattern

/**
 * Utility class for detecting and extracting Beans Bag links from chat messages
 */
object LinkDetector {

    // Patterns to detect beans bag messages
    private val BEANS_BAG_KEYWORDS = listOf(
        "beans bag",
        "beansbag",
        "bean bag",
        "click for more info",
        "reward link",
        "grab now"
    )

    // URL pattern for extracting links
    private val URL_PATTERN = Pattern.compile(
        "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]+)",
        Pattern.CASE_INSENSITIVE
    )

    /**
     * Check if a message contains beans bag keywords
     */
    fun containsBeansBag(message: String): Boolean {
        val lowerMessage = message.lowercase()
        return BEANS_BAG_KEYWORDS.any { keyword ->
            lowerMessage.contains(keyword)
        }
    }

    /**
     * Extract all URLs from a message
     */
    fun extractLinks(message: String): List<String> {
        val links = mutableListOf<String>()
        val matcher = URL_PATTERN.matcher(message)

        while (matcher.find()) {
            matcher.group(1)?.let { links.add(it) }
        }

        return links
    }

    /**
     * Parse a raw message into ChatMessage with beans bag detection
     */
    fun parseChatMessage(
        userId: String,
        username: String,
        messageText: String,
        messageId: String = UUID.randomUUID().toString()
    ): ChatMessage {
        val containsBeansBag = containsBeansBag(messageText)
        val links = if (containsBeansBag) extractLinks(messageText) else emptyList()

        return ChatMessage(
            id = messageId,
            userId = userId,
            username = username,
            message = messageText,
            timestamp = System.currentTimeMillis(),
            containsBeansBag = containsBeansBag,
            beansBagLinks = links,
            isHighlighted = containsBeansBag && links.isNotEmpty()
        )
    }

    /**
     * Convert chat message to BeansBag objects
     */
    fun extractBeansBags(chatMessage: ChatMessage): List<BeansBag> {
        if (!chatMessage.hasRewardLinks()) return emptyList()

        return chatMessage.beansBagLinks.map { link ->
            BeansBag(
                id = UUID.randomUUID().toString(),
                link = link,
                messageId = chatMessage.id,
                timestamp = chatMessage.timestamp,
                isActive = true,
                isClicked = false
            )
        }
    }

    /**
     * Deduplicate beans bags by link
     */
    fun deduplicateBeansBags(beansBags: List<BeansBag>): List<BeansBag> {
        val uniqueLinks = mutableMapOf<String, BeansBag>()

        // Keep only the most recent instance of each link
        beansBags.forEach { bag ->
            val existing = uniqueLinks[bag.link]
            if (existing == null || bag.timestamp > existing.timestamp) {
                uniqueLinks[bag.link] = bag
            }
        }

        return uniqueLinks.values
            .sortedByDescending { it.timestamp }
            .toList()
    }

    /**
     * Filter active beans bags (not clicked and not expired)
     */
    fun filterActiveBeansBags(beansBags: List<BeansBag>): List<BeansBag> {
        return beansBags.filter { !it.isClicked && !it.isExpired() }
    }
}
