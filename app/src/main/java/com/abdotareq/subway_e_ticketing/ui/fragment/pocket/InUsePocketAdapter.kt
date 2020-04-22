/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abdotareq.subway_e_ticketing.ui.fragment.pocket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.databinding.PocketInUseItemBinding
import com.abdotareq.subway_e_ticketing.model.InTicket
import com.abdotareq.subway_e_ticketing.model.TicketType

class InUsePocketAdapter(val clickListener: InUseTicketListener) : ListAdapter<InTicket,
        InUsePocketAdapter.ViewHolder>(InUseTicketDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

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