package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.TicketItemNewBinding
import com.abdotareq.subway_e_ticketing.model.TicketType

class TicketAdapter(val clickListener: TicketListener) : ListAdapter<TicketType,
        TicketAdapter.ViewHolder>(TicketDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // add animation
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_up)

        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: TicketItemNewBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: TicketListener, item: TicketType) {
            binding.ticket = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TicketItemNewBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
private class TicketDiffCallback : DiffUtil.ItemCallback<TicketType>() {
    override fun areItemsTheSame(oldItem: TicketType, newItem: TicketType): Boolean {
        return oldItem.price == newItem.price
    }

    override fun areContentsTheSame(oldItem: TicketType, newItem: TicketType): Boolean {
        return oldItem == newItem
    }
}

class TicketListener(val clickListener: (ticket: TicketType) -> Unit) {
    fun onClick(ticketType: TicketType) = clickListener(ticketType)
}