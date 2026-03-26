package com.beangrabber.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.beangrabber.R
import com.beangrabber.data.model.BeansBag
import com.beangrabber.databinding.ActivityMainBinding
import com.beangrabber.ui.adapter.BeansBagAdapter
import com.beangrabber.ui.adapter.ChatMessageAdapter
import kotlinx.coroutines.launch

/**
 * Main activity for the Bean Grabber app
 * Displays chat messages and active beans bags with grip system
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val chatViewModel: ChatViewModel by viewModels()
    private val accountViewModel: AccountViewModel by viewModels()

    private lateinit var chatAdapter: ChatMessageAdapter
    private lateinit var beansBagAdapter: BeansBagAdapter
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vibrator = getSystemService(Vibrator::class.java)

        setupChatRecyclerView()
        setupGripPanel()
        setupControls()
        observeViewModels()

        // Load sample data for testing
        chatViewModel.loadSampleData()
    }

    /**
     * Setup chat RecyclerView with optimizations
     */
    private fun setupChatRecyclerView() {
        chatAdapter = ChatMessageAdapter { link ->
            openLink(link)
        }

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                stackFromEnd = true // Start from bottom
                reverseLayout = false
            }
            adapter = chatAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20) // Performance optimization
        }
    }

    /**
     * Setup Grip System panel
     */
    private fun setupGripPanel() {
        beansBagAdapter = BeansBagAdapter { beansBag ->
            grabBeansBag(beansBag)
        }

        binding.gripRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = beansBagAdapter
            setHasFixedSize(false)
        }
    }

    /**
     * Setup control panel (filters, buttons, input)
     */
    private fun setupControls() {
        // Reward filter switch
        binding.rewardFilterSwitch.setOnCheckedChangeListener { _, isChecked ->
            chatViewModel.setRewardFilter(isChecked)
        }

        // Auto-click switch
        binding.autoClickSwitch.setOnCheckedChangeListener { _, isChecked ->
            chatViewModel.toggleAutoClick()
        }

        // Send message button
        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        // Accounts button
        binding.accountsButton.setOnClickListener {
            // TODO: Open accounts dialog
            showAccountsInfo()
        }

        // Statistics button
        binding.statisticsButton.setOnClickListener {
            showStatistics()
        }

        // Notification banner
        binding.notificationBanner.setOnClickListener {
            // Scroll to latest beans bag or open it
            val activeBags = beansBagAdapter.currentList
            if (activeBags.isNotEmpty()) {
                grabBeansBag(activeBags.first())
            }
        }

        binding.closeNotification.setOnClickListener {
            binding.notificationBanner.visibility = View.GONE
            chatViewModel.clearNotification()
        }
    }

    /**
     * Observe ViewModels for data changes
     */
    private fun observeViewModels() {
        // Observe chat messages
        lifecycleScope.launch {
            chatViewModel.getAllMessages().collect { messages ->
                chatAdapter.submitList(messages) {
                    // Auto-scroll to bottom on new message
                    if (messages.isNotEmpty()) {
                        binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
                    }
                }
            }
        }

        // Observe active beans bags
        lifecycleScope.launch {
            chatViewModel.getActiveBeansBags().collect { bags ->
                beansBagAdapter.submitList(bags)

                // Show/hide empty state
                if (bags.isEmpty()) {
                    binding.noActiveBagsText.visibility = View.VISIBLE
                    binding.gripRecyclerView.visibility = View.GONE
                } else {
                    binding.noActiveBagsText.visibility = View.GONE
                    binding.gripRecyclerView.visibility = View.VISIBLE
                }
            }
        }

        // Observe notification
        lifecycleScope.launch {
            chatViewModel.newBeansBagNotification.collect { beansBag ->
                if (beansBag != null) {
                    showNotification()
                    vibratePhone()
                }
            }
        }

        // Observe filter state
        lifecycleScope.launch {
            chatViewModel.showOnlyRewards.collect { showOnlyRewards ->
                binding.rewardFilterSwitch.isChecked = showOnlyRewards
            }
        }
    }

    /**
     * Send a chat message
     */
    private fun sendMessage() {
        val message = binding.messageInput.text.toString().trim()
        if (message.isNotEmpty()) {
            // Get active account or use default
            val account = accountViewModel.activeAccount.value
            val username = account?.username ?: "Guest"
            val userId = account?.id ?: "guest"

            chatViewModel.addMessage(userId, username, message)
            binding.messageInput.text.clear()
        }
    }

    /**
     * Grab a beans bag (open link and mark as clicked)
     */
    private fun grabBeansBag(beansBag: BeansBag) {
        // Mark as clicked
        chatViewModel.grabBeansBag(beansBag.id)

        // Open link
        openLink(beansBag.link)

        // Vibrate
        vibratePhone()

        // Hide notification
        chatViewModel.clearNotification()
        binding.notificationBanner.visibility = View.GONE
    }

    /**
     * Open a link in browser
     */
    private fun openLink(link: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Show notification banner
     */
    private fun showNotification() {
        binding.notificationBanner.visibility = View.VISIBLE
    }

    /**
     * Vibrate phone for feedback
     */
    private fun vibratePhone() {
        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }

    /**
     * Show statistics dialog
     */
    private fun showStatistics() {
        val stats = chatViewModel.statistics.value
        val message = """
            Statistics:

            Total Grabbed: ${stats.totalGrabbed}
            Successful: ${stats.successfulGrabs}
            Missed: ${stats.missedGrabs}
            Today: ${stats.todayGrabs}
            Success Rate: ${"%.1f".format(stats.getSuccessRate())}%
        """.trimIndent()

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Grab Statistics")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Show accounts info
     */
    private fun showAccountsInfo() {
        val accounts = accountViewModel.accounts.value
        val activeAccount = accountViewModel.activeAccount.value

        val message = if (accounts.isEmpty()) {
            "No accounts added yet.\n\nYou can use the app as a guest or add accounts through the account management system."
        } else {
            val accountList = accounts.joinToString("\n") { account ->
                val status = if (account.id == activeAccount?.id) "✓" else "○"
                "$status ${account.username}"
            }
            "Accounts:\n\n$accountList\n\nActive: ${activeAccount?.username ?: "Guest"}"
        }

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Account Management")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
