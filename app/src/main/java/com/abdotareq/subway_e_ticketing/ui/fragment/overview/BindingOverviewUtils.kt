package com.abdotareq.subway_e_ticketing.ui.fragment.overview

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.utility.AnimationUtil
import com.abdotareq.subway_e_ticketing.viewmodels.home.TripDetailsApiStatus

@BindingAdapter("setIsVisible")
fun setIsVisible(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.visibility = View.VISIBLE
        AnimationUtil.translateRightAnimate(view)
    } else {
        view.visibility = View.GONE
    }
}

/**
 * This binding adapter displays the [TicketTypeApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */

@BindingAdapter("tripImageApiStatus")
fun bindStatus(statusImageView: ImageView, statusType: TripDetailsApiStatus?) {
    when (statusType) {
        TripDetailsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        TripDetailsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        TripDetailsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}


@BindingAdapter("tripApiStatus")
fun tripApiStatus(statusImageView: View, statusType: TripDetailsApiStatus?) {
    when (statusType) {
        TripDetailsApiStatus.LOADING -> {
            statusImageView.visibility = View.GONE
        }
        TripDetailsApiStatus.ERROR -> {
            statusImageView.visibility = View.GONE
        }
        TripDetailsApiStatus.DONE -> {
            statusImageView.visibility = View.VISIBLE
        }
    }
}