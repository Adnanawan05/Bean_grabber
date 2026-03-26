package com.beangrabber.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beangrabber.R
import com.beangrabber.data.model.ChatMessage

/**
 * Optimized RecyclerView adapter for chat messages
 * Uses DiffUtil for efficient updates
 */
class ChatMessageAdapter(
    private val onLinkClick: (String) -> Unit
) : ListAdapter<ChatMessage, ChatMessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return MessageViewHolder(view, onLinkClick)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageViewHolder(
        itemView: View,
        private val onLinkClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val usernameText: TextView = itemView.findViewById(R.id.usernameText)
        private val messageText: TextView = itemView.findViewById(R.id.messageText)
        private val timeText: TextView = itemView.findViewById(R.id.timeText)
        private val linkButton: TextView = itemView.findViewById(R.id.linkButton)

        fun bind(message: ChatMessage) {
            usernameText.text = message.username
            messageText.text = message.message
            timeText.text = message.getFormattedTime()

            // Highlight beans bag messages
            if (message.isHighlighted) {
                itemView.setBackgroundColor(Color.parseColor("#FFD700")) // Gold
                messageText.setTextColor(Color.BLACK)
                usernameText.setTextColor(Color.BLACK)

                // Show link button
                if (message.beansBagLinks.isNotEmpty()) {
                    linkButton.visibility = View.VISIBLE
                    linkButton.text = "GRAB NOW"
                    linkButton.setOnClickListener {
                        onLinkClick(message.beansBagLinks.first())
                    }
                } else {
                    linkButton.visibility = View.GONE
                }
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
                messageText.setTextColor(Color.WHITE)
                usernameText.setTextColor(Color.LTGRAY)
                linkButton.visibility = View.GONE
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    class MessageDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}
