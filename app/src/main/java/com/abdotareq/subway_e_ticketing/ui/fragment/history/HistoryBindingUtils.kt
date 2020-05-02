package com.abdotareq.subway_e_ticketing.ui.fragment.history

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.History

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

