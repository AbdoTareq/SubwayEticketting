package com.abdotareq.subway_e_ticketing.ui.fragment.pocket

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.PocketInUseItemBinding
import com.abdotareq.subway_e_ticketing.model.InTicket
import timber.log.Timber

class InUsePocketAdapter : ListAdapter<InTicket,
        InUsePocketAdapter.ViewHolder>(InUseTicketDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // add animation
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_right)

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: PocketInUseItemBinding, val application: Context)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InTicket) {
            binding.inTicket = item

            binding.title.text = application.getString(R.string.check_out)
            binding.price.text = String.format(application.getString(R.string.ticket_price_format, item.price))
            binding.instructions.text = String.format(application.getString(R.string.scan_mess_format,
                    application.getString(R.string.exit), application.getString(R.string.check_out)))
            // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
            val qrgEncoder = QRGEncoder(item.id, null, QRGContents.Type.TEXT, 400)
            try {
                // Getting QR-Code as Bitmap
                val bitmap = qrgEncoder.bitmap
                // Setting Bitmap to ImageView
                binding.qrImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Timber.e(e)
            }

            binding.executePendingBindings()

            // this for foldable animation
            binding.foldingCell.setOnClickListener {
                binding.foldingCell.toggle(false)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PocketInUseItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.context)
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
