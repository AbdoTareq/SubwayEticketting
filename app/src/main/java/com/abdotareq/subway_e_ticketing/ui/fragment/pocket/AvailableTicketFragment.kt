package com.abdotareq.subway_e_ticketing.ui.fragment.pocket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.databinding.FragmentAvailableTicketBinding
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.viewmodels.factories.ViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.home.AvailableViewModel


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class AvailableTicketFragment : Fragment() {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AvailableViewModel

    private var _binding: FragmentAvailableTicketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAvailableTicketBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        val token = SharedPreferenceUtil.getSharedPrefsTokenId(context)

        viewModelFactory = ViewModelFactory(application, token)

        viewModel = ViewModelProvider(this, viewModelFactory).get(AvailableViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        val availableAdapter = AvailablePocketAdapter()
        // handle list change
        binding.availableTicketsList.adapter = availableAdapter

        binding.historyBtn.setOnClickListener {
            findNavController().navigate(AvailableTicketFragmentDirections
                    .actionAvailableTicketFragmentToHistoryFragment())
        }

        return view
    }

}
