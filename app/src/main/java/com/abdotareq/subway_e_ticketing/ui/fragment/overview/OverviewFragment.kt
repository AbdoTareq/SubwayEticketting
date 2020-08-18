package com.abdotareq.subway_e_ticketing.ui.fragment.overview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentOverviewBinding
import com.abdotareq.subway_e_ticketing.data.model.SearchModel
import com.abdotareq.subway_e_ticketing.ui.activity.BuyTicketActivity
import com.abdotareq.subway_e_ticketing.utility.AnimationUtil
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil.getSharedPrefsTokenId
import com.abdotareq.subway_e_ticketing.viewmodels.factories.ViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.home.OverviewViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import timber.log.Timber

/**
 * A [OverviewFragment] provides user with all needed info about metro trip and buy tickets.
 */
// TODO: 8/18/2020 fix no connection exception interceptor happened if u go to another fragment then returned
class OverviewFragment : Fragment() {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: OverviewViewModel

    private var _binding: FragmentOverviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        val token = getSharedPrefsTokenId(context)

        viewModelFactory = ViewModelFactory(application, token)

        viewModel = ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        AnimationUtil.fadeAnimate(binding.instructions)

        viewModel.eventBuy.observe(viewLifecycleOwner, Observer {
            if (it) {
                Intent(context, BuyTicketActivity::class.java).apply {
                    putExtra("ticket", viewModel.trip.value!!.ticketType)
                    startActivity(this)
                }
                viewModel.onEventBuyComplete()
            }
        })

        binding.start.setOnClickListener {
            SimpleSearchDialogCompat(requireContext(), getString(R.string.chooseStation), getString(R.string.search_stattions),
                    null, initData(), SearchResultListener { dialog, item, _ ->
                binding.start.text = item.title
                viewModel.startStationId.value = getStationId(item)

                binding.destination.isEnabled = true
                Toast.makeText(context, "${getStationId(item)}", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }).show()
        }

        binding.destination.setOnClickListener {
            SimpleSearchDialogCompat(requireContext(), getString(R.string.chooseStation), getString(R.string.search_stattions),
                    null, initData(), SearchResultListener { dialog, item, _ ->
                binding.destination.text = item.title
                viewModel.destinationStationId.value = getStationId(item)
                viewModel.onChooseStartDestinationComplete()
                dialog.dismiss()
            }).show()
        }

        return view
    }

    private fun getStationId(item: SearchModel): Int {
        for (i in viewModel.allStations.value!!) {
            if (item.title == i.stationName) {
                return i.stationId
            }
        }
        return -1
    }

    private fun initData(): ArrayList<SearchModel> {
        val list = ArrayList<SearchModel>()
        try {
            for (item in viewModel.stationsSearchList.value!!)
                list.add(SearchModel(item))
        } catch (e: Exception) {
            Timber.e(e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
        return list
    }

    override fun onDestroyView() {
        // this to save user data before destroy fragment or replace ir
        // (when select another fragment from bottom navigation view)
        super.onDestroyView()
        _binding = null
    }
}
