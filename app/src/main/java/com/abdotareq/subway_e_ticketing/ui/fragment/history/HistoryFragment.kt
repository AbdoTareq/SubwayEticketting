package com.abdotareq.subway_e_ticketing.ui.fragment.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.databinding.FragmentHistoryBinding
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.viewmodels.HistoryViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.HistoryViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {

    private lateinit var viewModelFactory: HistoryViewModelFactory
    private lateinit var viewModel: HistoryViewModel

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        val bearerToken = SharedPreferenceUtil.getSharedPrefsTokenId(context)

        viewModelFactory = HistoryViewModelFactory(bearerToken, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        val adapter = HistoryAdapter()
        binding.historyList.adapter = adapter
        // handle list change
        viewModel.historyTickets.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
