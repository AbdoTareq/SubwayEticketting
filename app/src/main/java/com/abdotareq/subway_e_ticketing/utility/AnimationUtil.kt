package com.abdotareq.subway_e_ticketing.utility

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AnimationUtils
import com.abdotareq.subway_e_ticketing.R

/**
 * A util class that contains a global static methods used across the App
 */
object AnimationUtil {
    fun fadeAnimate(view: View) {
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)
        animator.duration = 1500
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    fun translateRightAnimate(view: View) {
        view.animation = AnimationUtils.loadAnimation(view.context, R.anim.scale_down)
    }
}