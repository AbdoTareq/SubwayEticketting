package com.abdotareq.subway_e_ticketing.ui.fragment.pocket

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.BoughtTicket
import com.abdotareq.subway_e_ticketing.model.InTicket

// this for price in in-use item in pocket recycle view
@BindingAdapter("setTicketInUsePrice")
fun TextView.setTicketInUsePrice(item: InTicket?) {
    item?.let {
        text = String.format(
                context!!.getString(R.string.ticket_price_format, it.price))
    }
}


// this for price in available item in pocket recycle view
@BindingAdapter("setTicketAvailablePrice")
fun TextView.setTicketAvailablePrice(item: BoughtTicket?) {
    item?.let {
        text = String.format(
                context!!.getString(R.string.ticket_price_format, it.price))
    }
}

