package com.abdotareq.subway_e_ticketing.ui.fragment

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.History
import com.abdotareq.subway_e_ticketing.model.TicketType
import com.abdotareq.subway_e_ticketing.ui.fragment.history.HistoryAdapter
import com.abdotareq.subway_e_ticketing.ui.fragment.ticket.TicketAdapter


enum class ApiStatus { LOADING, ERROR, DONE, EMPTY }

/**
 * When there is no tickets history data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("historyListData")
fun bindHistoryRecyclerView(recyclerView: RecyclerView, data: List<History>?) {
    val adapter = recyclerView.adapter as HistoryAdapter
    adapter.submitList(data)
}
@BindingAdapter("ticketTypeListData")
fun bindTicketRecyclerView(recyclerView: RecyclerView, data: List<TicketType>?) {
    val adapter = recyclerView.adapter as TicketAdapter
    adapter.submitList(data)
}

/**
 * This binding adapter displays the [ApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("apiStatus")
fun bindStatus(statusImageView: ImageView, status: ApiStatus?) {
    when (status) {
        ApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        ApiStatus.EMPTY -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.empty_box)
        }
        else -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("progressApiStatus")
fun bindProgress(statusProgress: ProgressBar, statusType: ApiStatus?) {
    when (statusType) {
        ApiStatus.LOADING -> {
            statusProgress.visibility = View.VISIBLE
        }
        else -> {
            statusProgress.visibility = View.GONE
        }
    }
}

