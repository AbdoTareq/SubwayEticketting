package com.abdotareq.subway_e_ticketing.ui.fragment.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.databinding.FragmentTicketBinding
import com.abdotareq.subway_e_ticketing.viewmodels.TicketsViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.TicketViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Use the [TicketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TicketFragment : Fragment() {

    private lateinit var viewModelFactory: TicketViewModelFactory
    private lateinit var viewModel: TicketsViewModel

    private var _binding: FragmentTicketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        viewModelFactory = TicketViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TicketsViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        val adapter = TicketAdapter(TicketListener { price ->
            viewModel.onBuyTicket(price)
        })

        binding.ticketList.adapter = adapter
        // handle list change
        viewModel.tickets.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.eventBuyTicket.observe(viewLifecycleOwner, Observer {
            if (it > 0) {
//                Toast.makeText(context, "price: ${it}", Toast.LENGTH_LONG).show()
                openChangePassDialog()
                viewModel.onBuyTicketComplete()
            }
        })




        return view
    }

    // this for change pass dialog
    private fun openChangePassDialog() {
        val passDialog = BuyTicketDialogFragment()
        requireActivity().supportFragmentManager
                .let { passDialog.show(it, "Pass Dialog") }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
