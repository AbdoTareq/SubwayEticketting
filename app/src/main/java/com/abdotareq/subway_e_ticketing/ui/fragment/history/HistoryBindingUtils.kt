package com.abdotareq.subway_e_ticketing.ui.fragment.history

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.data.model.History
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

@BindingAdapter("setTicketPrice")
fun TextView.setTicketPrice(item: History?) {
    item?.let {
        text = String.format(
                context!!.getString(R.string.ticket_price_format, it.price))
    }
}

@BindingAdapter("setCheckInStationName")
fun TextView.setCheckInStationName(item: History?) {
    item?.let {
//        text = "Check-in: ${it.checkInStationName}"
        text = String.format(
                context!!.getString(R.string.check_in_format, it.checkInStationName))
    }
}

@BindingAdapter("setCheckInDate")
fun TextView.setCheckInDate(item: History?) {
    item?.let {
        text = "at: ${it.checkInDate}"
    }
}

@BindingAdapter("setCheckOutStationName")
fun TextView.setCheckOutStationName(item: History?) {
    item?.let {
//        text = "Check-out: ${it.checkOutStationName}"
        text = String.format(
                context!!.getString(R.string.check_out_format, it.checkOutStationName))

    }
}

@BindingAdapter("setCheckOutDate")
fun TextView.setCheckOutDate(item: History?) {
    item?.let {
        text = "at: ${it.checkOutDate}"
    }
}

@BindingAdapter("setHistoryColor")
fun View.setHistoryColor(item: History?) {
    item?.let {
        try {
            setBackgroundColor(Color.parseColor("#${it.color_code}"))
        } catch (e: Exception) {
            Timber.e(e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}

