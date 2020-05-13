package com.abdotareq.subway_e_ticketing.ui.fragment.pocket

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.BoughtTicket
import com.abdotareq.subway_e_ticketing.model.InTicket
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ramotion.foldingcell.FoldingCell
import timber.log.Timber

// this for price in in-use item in pocket recycle view
@BindingAdapter("setTicketInUsePrice")
fun TextView.setTicketInUsePrice(item: InTicket?) {
    item?.let {
        text = String.format(
                context!!.getString(R.string.ticket_price_format, it.price))
    }
}

@BindingAdapter("setTicketInUseColor")
fun View.setTicketInUseColor(item: InTicket?) {
    item?.let {
        try {
            setBackgroundColor(Color.parseColor("#${it.color_code}"))
        } catch (e: Exception) {
            Timber.e(e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
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

@BindingAdapter("setTicketAvailableColor")
fun View.setTicketAvailableColor(item: BoughtTicket?) {
    item?.let {
        try {
            setBackgroundColor(Color.parseColor("#${it.color_code}"))
        } catch (e: Exception) {
            Timber.e(e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}





