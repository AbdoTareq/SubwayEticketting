package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.BuyTicketDialogFragmentBinding
import com.abdotareq.subway_e_ticketing.model.Ticket
import com.abdotareq.subway_e_ticketing.viewmodels.BuyTicketViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.BuyTicketViewModelFactory


class BuyTicketDialogFragment(val ticket: Ticket) : AppCompatDialogFragment() {

    private lateinit var viewModelFactory: BuyTicketViewModelFactory
    private lateinit var viewModel: BuyTicketViewModel

    private lateinit var binding: BuyTicketDialogFragmentBinding

//    private var ticket: Ticket = ticket

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BuyTicketDialogFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        viewModelFactory = BuyTicketViewModelFactory(ticket, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(BuyTicketViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // show ticket number
        viewModel.ticketNum.observe(viewLifecycleOwner, Observer {
            if (it > 0) {
                val cost = viewModel.ticketNum.value!! * ticket.price
                Toast.makeText(context, "${viewModel.ticketNum.value}", Toast.LENGTH_SHORT).show()
                binding.totalCost.text = String.format(
                        context!!.getString(R.string.ticket_cost_format, cost))
            }

        })


        return view
    }


}