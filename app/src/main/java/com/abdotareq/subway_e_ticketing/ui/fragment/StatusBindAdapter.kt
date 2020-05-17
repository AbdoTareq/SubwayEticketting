package com.abdotareq.subway_e_ticketing.ui.fragment

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.data.model.History
import com.abdotareq.subway_e_ticketing.data.model.InTicket
import com.abdotareq.subway_e_ticketing.data.model.TicketType
import com.abdotareq.subway_e_ticketing.ui.fragment.history.HistoryAdapter
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.AvailablePocketAdapter
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.InUsePocketAdapter
import com.abdotareq.subway_e_ticketing.ui.fragment.ticket.TicketAdapter
import com.abdotareq.subway_e_ticketing.utility.AnimationUtil

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
@BindingAdapter("checkInListData")
fun bindCheckInRecyclerView(recyclerView: RecyclerView, data: List<InTicket>?) {
    val adapter = recyclerView.adapter as InUsePocketAdapter
    adapter.submitList(data)
}
@BindingAdapter("availableListData")
fun bindAvailableRecyclerView(recyclerView: RecyclerView, data: List<InTicket>?) {
    val adapter = recyclerView.adapter as AvailablePocketAdapter
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
fun bindProgress(statusProgress: View, statusType: ApiStatus?) {
    when (statusType) {
        ApiStatus.LOADING -> {
            statusProgress.visibility = View.VISIBLE
        }
        else -> {
            statusProgress.visibility = View.GONE
        }
    }
}

// bind start button & text instructions to display if data is available
@BindingAdapter("stationStatus")
fun bindStart(view: View, statusType: ApiStatus?) {
    when (statusType) {
        ApiStatus.DONE -> {
            view.visibility = View.VISIBLE
            AnimationUtil.translateRightAnimate(view)
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}

