package com.abdotareq.subway_e_ticketing.ui.fragment.pocket

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.BoughtTicket
import com.abdotareq.subway_e_ticketing.model.InTicket
import com.abdotareq.subway_e_ticketing.viewmodels.BoughtApiStatus
import com.abdotareq.subway_e_ticketing.viewmodels.InUseApiStatus


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
 * This binding adapter displays the [InUseApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("inUseApiStatus")
fun bindInUseStatus(statusImageView: ImageView, statusType: InUseApiStatus?) {
    when (statusType) {
        InUseApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        InUseApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        InUseApiStatus.EMPTY -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.empty_box)
        }
        InUseApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

/**
 * This binding adapter displays the [BoughtApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("boughtApiStatus")
fun bindBoughtStatus(statusImageView: ImageView, statusType: BoughtApiStatus?) {
    when (statusType) {
        BoughtApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        BoughtApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        BoughtApiStatus.EMPTY -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.empty_box)
        }
        BoughtApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}