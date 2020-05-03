package com.abdotareq.subway_e_ticketing.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abdotareq.subway_e_ticketing.databinding.FragmentOverviewBinding


/**
 * A simple [Fragment] subclass.
 * Use the [OverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

}
