package com.abdotareq.subway_e_ticketing.ui.fragment.history

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.History
import com.abdotareq.subway_e_ticketing.viewmodels.TicketApiStatus


/**
 * When there is no tickets history data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<History>?) {
    val adapter = recyclerView.adapter as HistoryAdapter
    adapter.submitList(data)
}

/**
 * This binding adapter displays the [TicketApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("ticketApiStatus")
fun bindStatus(statusImageView: ImageView, status: TicketApiStatus?) {
    when (status) {
        TicketApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        TicketApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        TicketApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}