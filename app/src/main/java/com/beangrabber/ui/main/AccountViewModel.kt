package com.beangrabber.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.beangrabber.data.model.UserAccount
import com.beangrabber.util.AccountManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel for managing user accounts
 */
class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val accountManager = AccountManager(application)

    private val _accounts = MutableStateFlow<List<UserAccount>>(emptyList())
    val accounts: StateFlow<List<UserAccount>> = _accounts.asStateFlow()

    private val _activeAccount = MutableStateFlow<UserAccount?>(null)
    val activeAccount: StateFlow<UserAccount?> = _activeAccount.asStateFlow()

    init {
        loadAccounts()
    }

    /**
     * Load accounts from storage
     */
    private fun loadAccounts() {
        viewModelScope.launch {
            _accounts.value = accountManager.getAllAccounts()
            _activeAccount.value = accountManager.getActiveAccount()
        }
    }

    /**
     * Add a new account
     */
    fun addAccount(username: String, token: String): Boolean {
        val account = UserAccount(
            id = UUID.randomUUID().toString(),
            username = username,
            token = token,
            isActive = false
        )

        val success = accountManager.addAccount(account)
        if (success) {
            loadAccounts()
        }
        return success
    }

    /**
     * Switch to a different account
     */
    fun switchAccount(accountId: String) {
        viewModelScope.launch {
            accountManager.setActiveAccount(accountId)
            loadAccounts()
        }
    }

    /**
     * Remove an account
     */
    fun removeAccount(accountId: String) {
        viewModelScope.launch {
            accountManager.removeAccount(accountId)
            loadAccounts()
        }
    }

    /**
     * Logout current account
     */
    fun logout() {
        viewModelScope.launch {
            accountManager.clearActiveAccount()
            loadAccounts()
        }
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return _activeAccount.value != null
    }

    /**
     * Get account count
     */
    fun getAccountCount(): Int {
        return _accounts.value.size
    }
}
