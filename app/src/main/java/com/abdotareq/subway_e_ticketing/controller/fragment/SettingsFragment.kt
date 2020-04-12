package com.abdotareq.subway_e_ticketing.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abdotareq.subway_e_ticketing.databinding.FragmentSettingsBinding

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
