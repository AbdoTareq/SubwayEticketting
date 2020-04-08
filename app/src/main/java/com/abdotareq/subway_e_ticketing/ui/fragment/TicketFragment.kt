package com.abdotareq.subway_e_ticketing.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.abdotareq.subway_e_ticketing.R


/**
 * A simple [Fragment] subclass.
 * Use the [TicketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TicketFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }


}
