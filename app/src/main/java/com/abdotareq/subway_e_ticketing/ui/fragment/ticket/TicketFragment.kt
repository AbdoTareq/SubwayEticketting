package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.databinding.FragmentTicketBinding
import com.abdotareq.subway_e_ticketing.model.TicketType
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.viewmodels.TicketsTypeViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.TicketViewModelFactory
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class TicketFragment : Fragment() {

    private lateinit var viewModelFactory: TicketViewModelFactory
    private lateinit var viewModel: TicketsTypeViewModel

    private var _binding: FragmentTicketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        val token = SharedPreferenceUtil.getSharedPrefsTokenId(context)

        viewModelFactory = TicketViewModelFactory(token, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TicketsTypeViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        val adapter = TicketAdapter(TicketListener { price ->
            viewModel.onChooseTicket(price)
        })
        binding.ticketList.adapter = adapter
        // handle list change

        viewModel.eventChooseTicket.observe(viewLifecycleOwner, Observer { ticket_price ->
            if (ticket_price > 0) {

                var ticketTemp = TicketType()
                for (ticket in viewModel.ticketsType.value!!) {
                    if (ticket_price == ticket.price)
                        ticketTemp = ticket
                }
                openChangePassDialog(ticketTemp)
                viewModel.onChooseTicketComplete()
            }
        })



        return view
    }

    // this for change pass dialog
    private fun openChangePassDialog(ticketTypeTemp: TicketType) {
        val dialog = BuyTicketDialogFragment(ticketTypeTemp)
        requireActivity().supportFragmentManager
                .let { dialog.show(it, "Buy Dialog") }
    }

    override fun onDestroyView() {
        // this to save user data before destroy fragment or replace ir
        // (when select another fragment from bottom navigation view)
        super.onDestroyView()
        _binding = null
    }
}
