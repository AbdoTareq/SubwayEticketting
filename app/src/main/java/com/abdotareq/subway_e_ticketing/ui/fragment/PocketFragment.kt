package com.abdotareq.subway_e_ticketing.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.databinding.FragmentPocketBinding
import com.abdotareq.subway_e_ticketing.model.InTicket
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.AvailablePocketAdapter
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.AvailableTicketListener
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.InUsePocketAdapter
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.InUseTicketListener
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.viewmodels.PocketViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.PocketViewModelFactory
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class PocketFragment : Fragment() {

    private lateinit var viewModelFactory: PocketViewModelFactory
    private lateinit var viewModel: PocketViewModel

    private var _binding: FragmentPocketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPocketBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        val token = SharedPreferenceUtil.getSharedPrefsTokenId(context)

        viewModelFactory = PocketViewModelFactory(token, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PocketViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        val inUseAdapter = InUsePocketAdapter(InUseTicketListener { price ->
            viewModel.onChooseCheckInTicket(price)
        })
        // handle list change
        binding.inUseTicketsList.adapter = inUseAdapter

         val availableAdapter = AvailablePocketAdapter(AvailableTicketListener { price ->
            viewModel.onChooseCheckInTicket(price)
        })
        // handle list change
        binding.availableTicketsList.adapter = availableAdapter

        viewModel.eventChooseCheckInTicket.observe(viewLifecycleOwner, Observer { ticket_price ->
            if (ticket_price > 0) {

                var ticketTemp = InTicket()
                for (ticket in viewModel.checkInTickets.value!!) {
                    if (ticket_price == ticket.price)
                        ticketTemp = ticket
                }
                Timber.e("${ticketTemp}")
                viewModel.onChooseCheckInComplete()
            }
        })



        return view
    }

}
