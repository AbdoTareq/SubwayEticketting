package com.abdotareq.subway_e_ticketing.ui.fragment.pocket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.databinding.FragmentPocketBinding
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

        val inUseAdapter = InUsePocketAdapter()
        // handle list change
        binding.inUseTicketsList.adapter = inUseAdapter

        val availableAdapter = AvailablePocketAdapter()
        // handle list change
        binding.availableTicketsList.adapter = availableAdapter

        return view
    }

}
