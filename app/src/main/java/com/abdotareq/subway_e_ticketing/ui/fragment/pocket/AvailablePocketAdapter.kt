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
import com.abdotareq.subway_e_ticketing.data.model.InTicket
import com.abdotareq.subway_e_ticketing.databinding.PocketAvailableItemBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class AvailablePocketAdapter : ListAdapter<InTicket,
        AvailablePocketAdapter.ViewHolder>(AvailableTicketDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // add animation
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_left)

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: PocketAvailableItemBinding, val application: Context)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InTicket) {
            binding.boughtTicket = item

            binding.title.text = application.getString(R.string.check_in)
            binding.price.text = String.format(application.getString(R.string.ticket_price_format, item.price))
            binding.instructions.text = String.format(application.getString(R.string.scan_mess_format,
                    application.getString(R.string.entrance)))
            // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
            val qrgEncoder = QRGEncoder(item.id, null, QRGContents.Type.TEXT, 400)
            try {
                // Getting QR-Code as Bitmap
                val bitmap = qrgEncoder.bitmap
                // Setting Bitmap to ImageView
                binding.qrImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Timber.e(e)
            FirebaseCrashlytics.getInstance().recordException(e)
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
                val binding = PocketAvailableItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.context)
            }
        }
    }
}

private class AvailableTicketDiffCallback : DiffUtil.ItemCallback<InTicket>() {
    override fun areItemsTheSame(oldItem: InTicket, newItem: InTicket): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: InTicket, newItem: InTicket): Boolean {
        return oldItem == newItem
    }
}

//sealed class DataItem {
//    data class TicketItem(val sleepNight: BoughtTicket): DataItem() {
//        override val id = sleepNight.nightId
//    }
//
//    object Header: DataItem() {
//        override val id = Long.MIN_VALUE
//    }
//
//    abstract val id: Long
//}
