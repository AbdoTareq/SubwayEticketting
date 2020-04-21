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

package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.databinding.TicketItemBinding
import com.abdotareq.subway_e_ticketing.model.TicketType

class TicketAdapter(val clickListener: TicketListener) : ListAdapter<TicketType,
        TicketAdapter.ViewHolder>(TicketDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(clickListener,item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: TicketItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: TicketListener, item: TicketType) {
            binding.ticket = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TicketItemBinding.inflate(layoutInflater, parent, false)

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
class TicketDiffCallback : DiffUtil.ItemCallback<TicketType>() {
    override fun areItemsTheSame(oldItem: TicketType, newItem: TicketType): Boolean {
        return oldItem.price == newItem.price
    }

    override fun areContentsTheSame(oldItem: TicketType, newItem: TicketType): Boolean {
        return oldItem == newItem
    }
}

class TicketListener(val clickListener: (price: Int) -> Unit) {
    fun onClick(ticketType: TicketType) = clickListener(ticketType.price)
}