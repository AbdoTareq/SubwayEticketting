package com.abdotareq.subway_e_ticketing.ui.fragment.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abdotareq.subway_e_ticketing.BuildConfig
import com.abdotareq.subway_e_ticketing.databinding.FragmentSettingsBinding
import com.abdotareq.subway_e_ticketing.ui.activity.OnBoardActivity
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil


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

        binding.showTitle.setOnClickListener {
            val intent = Intent(context, OnBoardActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }

        binding.switchButton.isChecked = SharedPreferenceUtil.getSharedPrefsNightMode(context)

        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                SharedPreferenceUtil.setSharedPrefsNightMode(context, true)
            } else {
                // The toggle is disabled
                SharedPreferenceUtil.setSharedPrefsNightMode(context, false)
            }
            restartApp()
        }

        binding.rate.setOnClickListener {
            rate()
        }

        return view
    }

    private fun rate() {
        try {
            val marketUri = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            val marketUri = Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        }
    }

    private fun restartApp() {
        requireActivity().recreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
