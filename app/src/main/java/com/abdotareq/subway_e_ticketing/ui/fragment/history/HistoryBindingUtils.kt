/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abdotareq.subway_e_ticketing.ui.fragment.history

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.model.History

// TODO CHANGE TICKET TO Out_Ticket
@BindingAdapter("setTicketPrice")
fun TextView.setTicketPrice(item: History?) {
    item?.let {
        text = "${it.price} LE / Ticket"
    }
}

@BindingAdapter("setCheckInStation")
fun TextView.setCheckInStation(item: History?) {
    item?.let {
        text = "Check-in: ${it.checkInStation}"
    }
}

@BindingAdapter("setCheckInDate")
fun TextView.setCheckInDate(item: History?) {
    item?.let {
        text = "at: ${it.checkInDate}"
    }
}

@BindingAdapter("setCheckOutStation")
fun TextView.setCheckOutStation(item: History?) {
    item?.let {
        text = "Check-out: ${it.checkOutStation}"
    }
}

@BindingAdapter("setCheckOutDate")
fun TextView.setCheckOutDate(item: History?) {
    item?.let {
        text = "at: ${it.checkOutDate}"
    }
}
