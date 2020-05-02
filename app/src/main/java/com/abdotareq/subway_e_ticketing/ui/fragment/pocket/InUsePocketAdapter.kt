package com.abdotareq.subway_e_ticketing.ui.fragment.pocket

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.PocketInUseItemBinding
import com.abdotareq.subway_e_ticketing.model.InTicket

class InUsePocketAdapter(val clickListener: InUseTicketListener) : ListAdapter<InTicket,
        InUsePocketAdapter.ViewHolder>(InUseTicketDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // add animation
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_right)

        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: PocketInUseItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: InUseTicketListener, item: InTicket) {
            binding.inTicket = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PocketInUseItemBinding.inflate(layoutInflater, parent, false)

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
private class InUseTicketDiffCallback : DiffUtil.ItemCallback<InTicket>() {
    override fun areItemsTheSame(oldItem: InTicket, newItem: InTicket): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: InTicket, newItem: InTicket): Boolean {
        return oldItem == newItem
    }
}

class InUseTicketListener(val clickListener: (id: String) -> Unit) {
    fun onClick(inTicket: InTicket) = clickListener(inTicket.id!!)
}