package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.abdotareq.subway_e_ticketing.databinding.BuyTicketDialogFragmentBinding


class BuyTicketDialogFragment() : AppCompatDialogFragment() {

    private lateinit var binding: BuyTicketDialogFragmentBinding

//    private var ticket: Ticket = ticket

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BuyTicketDialogFragmentBinding.inflate(inflater, container, false)
        val view = binding.root





        return view
    }


}