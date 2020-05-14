package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.data.model.TicketType


@BindingAdapter("setTicketTypePrice")
fun TextView.setTicketTypePrice(item: TicketType?) {
    item?.let {
        text = String.format(
                context!!.getString(R.string.ticket_price_format, it.price))
    }
}

@BindingAdapter("setTicketStations")
fun TextView.setTicketStations(item: TicketType?) {
    item?.let {
        text = "${it.ticketInfo}"
    }
}

@BindingAdapter("setTicketTitle")
fun TextView.setTicketTitle(item: TicketType?) {
    item?.let {
        text = String.format(
                context!!.getString(R.string.ticket_title_format, it.color))
    }
}

@BindingAdapter("setTicketColor")
fun View.setTicketColor(item: TicketType?) {
    item?.let {
        setBackgroundColor(Color.parseColor("#${it.color_code}")
        )
    }
}

