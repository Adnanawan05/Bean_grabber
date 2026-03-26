package com.beangrabber.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beangrabber.R
import com.beangrabber.data.model.BeansBag

/**
 * Adapter for displaying active beans bags in the grip panel
 */
class BeansBagAdapter(
    private val onGrabClick: (BeansBag) -> Unit
) : ListAdapter<BeansBag, BeansBagAdapter.BeansBagViewHolder>(BeansBagDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeansBagViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_beans_bag, parent, false)
        return BeansBagViewHolder(view, onGrabClick)
    }

    override fun onBindViewHolder(holder: BeansBagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BeansBagViewHolder(
        itemView: View,
        private val onGrabClick: (BeansBag) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val linkText: TextView = itemView.findViewById(R.id.linkText)
        private val grabButton: TextView = itemView.findViewById(R.id.grabButton)
        private val timeText: TextView = itemView.findViewById(R.id.timeText)

        fun bind(beansBag: BeansBag) {
            // Show shortened link
            val shortLink = if (beansBag.link.length > 40) {
                beansBag.link.take(37) + "..."
            } else {
                beansBag.link
            }
            linkText.text = shortLink

            // Calculate time ago
            val minutesAgo = (System.currentTimeMillis() - beansBag.timestamp) / 60000
            timeText.text = when {
                minutesAgo < 1 -> "Just now"
                minutesAgo == 1L -> "1 min ago"
                else -> "$minutesAgo mins ago"
            }

            grabButton.setOnClickListener {
                onGrabClick(beansBag)
            }
        }
    }

    class BeansBagDiffCallback : DiffUtil.ItemCallback<BeansBag>() {
        override fun areItemsTheSame(oldItem: BeansBag, newItem: BeansBag): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BeansBag, newItem: BeansBag): Boolean {
            return oldItem == newItem
        }
    }
}
