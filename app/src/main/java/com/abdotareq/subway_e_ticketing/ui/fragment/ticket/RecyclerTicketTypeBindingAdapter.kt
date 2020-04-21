package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.TicketType
import com.abdotareq.subway_e_ticketing.viewmodels.TicketTypeApiStatus


/**
 * When there is no tickets history data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("ticketTypeListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<TicketType>?) {
    val adapter = recyclerView.adapter as TicketAdapter
    adapter.submitList(data)
}

/**
 * This binding adapter displays the [TicketTypeApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("ticketTypeApiStatus")
fun bindStatus(statusImageView: ImageView, statusType: TicketTypeApiStatus?) {
    when (statusType) {
        TicketTypeApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        TicketTypeApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        TicketTypeApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}