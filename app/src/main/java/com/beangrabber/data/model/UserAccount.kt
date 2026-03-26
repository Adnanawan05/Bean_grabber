package com.beangrabber.data.model

/**
 * Represents a user account in the multi-account system
 */
data class UserAccount(
    val id: String,
    val username: String,
    val token: String,
    val isActive: Boolean = false,
    val lastLoginTime: Long = System.currentTimeMillis(),
    val profileImageUrl: String? = null
) {
    /**
     * Activate this account
     */
    fun activate(): UserAccount {
        return copy(isActive = true, lastLoginTime = System.currentTimeMillis())
    }

    /**
     * Deactivate this account
     */
    fun deactivate(): UserAccount {
        return copy(isActive = false)
    }
}
