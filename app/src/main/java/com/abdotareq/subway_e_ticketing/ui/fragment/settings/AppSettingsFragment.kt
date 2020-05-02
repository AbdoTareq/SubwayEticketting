package com.abdotareq.subway_e_ticketing.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.databinding.FragmentAppSettingsBinding
import com.abdotareq.subway_e_ticketing.ui.fragment.AppSettingsFragmentDirections

/**
 * A simple [Fragment] subclass.
 */
class AppSettingsFragment : Fragment() {
    private var _binding: FragmentAppSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAppSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.profile.setOnClickListener {
            findNavController().navigate(AppSettingsFragmentDirections.actionAppSettingsFragmentToProfileSettingsFragment())
        }

        binding.appSettings.setOnClickListener {
            findNavController().navigate(AppSettingsFragmentDirections.actionAppSettingsFragmentToSettingsFragment())
        }

        return view
    }

}
