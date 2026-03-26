package com.beangrabber.util

import android.content.Context
import android.content.SharedPreferences
import com.beangrabber.data.model.UserAccount
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Manager for storing and retrieving user accounts using SharedPreferences
 */
class AccountManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "bean_grabber_accounts"
        private const val KEY_ACCOUNTS = "accounts"
        private const val KEY_ACTIVE_ACCOUNT_ID = "active_account_id"
    }

    /**
     * Get all stored accounts
     */
    fun getAllAccounts(): List<UserAccount> {
        val json = prefs.getString(KEY_ACCOUNTS, null) ?: return emptyList()
        val type = object : TypeToken<List<UserAccount>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * Save accounts to storage
     */
    private fun saveAccounts(accounts: List<UserAccount>) {
        val json = gson.toJson(accounts)
        prefs.edit().putString(KEY_ACCOUNTS, json).apply()
    }

    /**
     * Add a new account
     */
    fun addAccount(account: UserAccount): Boolean {
        val accounts = getAllAccounts().toMutableList()

        // Check if account already exists
        if (accounts.any { it.id == account.id || it.username == account.username }) {
            return false
        }

        accounts.add(account)
        saveAccounts(accounts)
        return true
    }

    /**
     * Update an existing account
     */
    fun updateAccount(account: UserAccount) {
        val accounts = getAllAccounts().toMutableList()
        val index = accounts.indexOfFirst { it.id == account.id }

        if (index != -1) {
            accounts[index] = account
            saveAccounts(accounts)
        }
    }

    /**
     * Remove an account
     */
    fun removeAccount(accountId: String) {
        val accounts = getAllAccounts().toMutableList()
        accounts.removeIf { it.id == accountId }
        saveAccounts(accounts)

        // Clear active account if it was removed
        if (getActiveAccountId() == accountId) {
            clearActiveAccount()
        }
    }

    /**
     * Get active account ID
     */
    fun getActiveAccountId(): String? {
        return prefs.getString(KEY_ACTIVE_ACCOUNT_ID, null)
    }

    /**
     * Set active account
     */
    fun setActiveAccount(accountId: String) {
        prefs.edit().putString(KEY_ACTIVE_ACCOUNT_ID, accountId).apply()

        // Update account statuses
        val accounts = getAllAccounts().map { account ->
            if (account.id == accountId) {
                account.activate()
            } else {
                account.deactivate()
            }
        }
        saveAccounts(accounts)
    }

    /**
     * Get active account
     */
    fun getActiveAccount(): UserAccount? {
        val accountId = getActiveAccountId() ?: return null
        return getAllAccounts().find { it.id == accountId }
    }

    /**
     * Clear active account
     */
    fun clearActiveAccount() {
        prefs.edit().remove(KEY_ACTIVE_ACCOUNT_ID).apply()

        // Deactivate all accounts
        val accounts = getAllAccounts().map { it.deactivate() }
        saveAccounts(accounts)
    }

    /**
     * Clear all accounts
     */
    fun clearAllAccounts() {
        prefs.edit().clear().apply()
    }
}
