package com.abdotareq.subway_e_ticketing.ui.fragment

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.databinding.FragmentOverviewBinding
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.viewmodels.factories.OverviewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.home.OverviewViewModel


/**
 * A [OverviewFragment] provides user with all needed info about metro trip and buy tickets.
 */
class OverviewFragment : Fragment() {

    private lateinit var viewModelFactory: OverviewModelFactory
    private lateinit var viewModel: OverviewViewModel

    private var _binding: FragmentOverviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        val token = SharedPreferenceUtil.getSharedPrefsTokenId(context)

        viewModelFactory = OverviewModelFactory(token, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        animateInstructions()

        binding.start.setOnClickListener {

        }


        return view
    }

    private fun animateInstructions() {
        val animator = ObjectAnimator.ofFloat(binding.instructions, View.ALPHA, 0f)
        animator.duration = 10000
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }


    override fun onDestroyView() {
        // this to save user data before destroy fragment or replace ir
        // (when select another fragment from bottom navigation view)
        super.onDestroyView()
        _binding = null
    }
}
