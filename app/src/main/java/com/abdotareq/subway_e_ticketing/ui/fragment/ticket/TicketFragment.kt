package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.databinding.FragmentTicketBinding
import com.abdotareq.subway_e_ticketing.ui.activity.BuyTicketActivity
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.viewmodels.factories.ViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.home.TicketsTypeViewModel


/**
 * A simple [Fragment] subclass.
 */
class TicketFragment : Fragment() {

    private lateinit var viewModelFactory: ViewModelFactory
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

        viewModelFactory = ViewModelFactory(application, token)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TicketsTypeViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // handle list change
        binding.ticketList.adapter = TicketAdapter(TicketListener {
            viewModel.onChooseTicket(it)
        })

        viewModel.eventChooseTicket.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                val intent = Intent(context, BuyTicketActivity::class.java)
                intent.putExtra("ticket", it)
                startActivity(intent)
                viewModel.onChooseTicketComplete()
            }
        })

        return view
    }

    override fun onDestroyView() {
        // this to save user data before destroy fragment or replace ir
        // (when select another fragment from bottom navigation view)
        super.onDestroyView()
        _binding = null
    }
}
