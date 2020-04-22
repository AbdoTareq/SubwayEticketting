package com.abdotareq.subway_e_ticketing.ui.fragment.pocket

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.BoughtTicket
import com.abdotareq.subway_e_ticketing.model.InTicket
import com.abdotareq.subway_e_ticketing.model.TicketType
import com.abdotareq.subway_e_ticketing.viewmodels.PocketApiStatus
import com.abdotareq.subway_e_ticketing.viewmodels.TicketTypeApiStatus


/**
 * When there is no tickets history data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("checkInListData")
fun bindCheckInRecyclerView(recyclerView: RecyclerView, data: List<InTicket>?) {
    val adapter = recyclerView.adapter as InUsePocketAdapter
    adapter.submitList(data)
}

@BindingAdapter("availableListData")
fun bindAvailableRecyclerView(recyclerView: RecyclerView, data: List<BoughtTicket>?) {
    val adapter = recyclerView.adapter as AvailablePocketAdapter
    adapter.submitList(data)
}

/**
 * This binding adapter displays the [PocketApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("PocketApiStatus")
fun bindStatus(statusImageView: ImageView, statusType: PocketApiStatus?) {
    when (statusType) {
        PocketApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        PocketApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        PocketApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}