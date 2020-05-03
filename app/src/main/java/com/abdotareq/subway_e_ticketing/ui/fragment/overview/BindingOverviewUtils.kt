package com.abdotareq.subway_e_ticketing.ui.fragment.overview

import android.view.View
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.utility.AnimationUtil


@BindingAdapter("setIsVisible")
fun setIsVisible(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.visibility = View.VISIBLE
        AnimationUtil.translateRightAnimate(view)
    } else {
        view.visibility = View.GONE
    }
}

