package com.abdotareq.subway_e_ticketing.ui.fragment.history

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.History
import com.abdotareq.subway_e_ticketing.viewmodels.home.HistoryApiStatus


/**
 * When there is no tickets history data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("historyListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<History>?) {
    val adapter = recyclerView.adapter as HistoryAdapter
    adapter.submitList(data)
}

/**
 * This binding adapter displays the [HistoryApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("historyApiStatus")
fun bindStatus(statusImageView: ImageView, status: HistoryApiStatus?) {
    when (status) {
        HistoryApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        HistoryApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        HistoryApiStatus.EMPTY -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.empty_box)
        }
        HistoryApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}