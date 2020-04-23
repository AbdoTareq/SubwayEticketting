package com.abdotareq.subway_e_ticketing.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.databinding.FragmentPocketBinding
import com.abdotareq.subway_e_ticketing.model.BoughtTicket
import com.abdotareq.subway_e_ticketing.model.InTicket
import com.abdotareq.subway_e_ticketing.ui.activity.ScanPocketActivity
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.AvailablePocketAdapter
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.AvailableTicketListener
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.InUsePocketAdapter
import com.abdotareq.subway_e_ticketing.ui.fragment.pocket.InUseTicketListener
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.viewmodels.home.PocketViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.PocketViewModelFactory


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

        val inUseAdapter = InUsePocketAdapter(InUseTicketListener { id ->
            viewModel.onChooseCheckInTicket(id)
        })
        // handle list change
        binding.inUseTicketsList.adapter = inUseAdapter

        val availableAdapter = AvailablePocketAdapter(AvailableTicketListener { id ->
            viewModel.onChooseBoughtTicket(id)
        })
        // handle list change
        binding.availableTicketsList.adapter = availableAdapter

        // Handle pocket check-in ticket click
        viewModel.eventChooseCheckInTicket.observe(viewLifecycleOwner, Observer { ticket_id ->
            if (!ticket_id.isNullOrEmpty()) {

                var ticketTemp = InTicket()
                for (ticket in viewModel.checkInTickets.value!!) {
                    if (ticket_id == ticket.id)
                        ticketTemp = ticket
                }
                viewModel.onChooseCheckInComplete()
                val intent = Intent(context,ScanPocketActivity::class.java)
                intent.putExtra("checkInTicket", ticketTemp) // sending ticket object.
                startActivity(intent)
            }
        })

        // Handle pocket bought ticket click
        viewModel.eventChooseBoughtTicket.observe(viewLifecycleOwner, Observer { ticket_id ->
            if (!ticket_id.isNullOrEmpty()) {

                var ticketTemp = BoughtTicket()
                for (ticket in viewModel.boughtTickets.value!!) {
                    if (ticket_id == ticket.id)
                        ticketTemp = ticket
                }
                viewModel.onChooseBoughtTicketComplete()
                val intent = Intent(context,ScanPocketActivity::class.java)
                intent.putExtra("boughtTicket", ticketTemp)// sending ticket object.
                startActivity(intent)
            }
        })



        return view
    }

}
