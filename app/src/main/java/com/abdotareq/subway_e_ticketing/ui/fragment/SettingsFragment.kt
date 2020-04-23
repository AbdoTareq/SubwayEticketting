package com.abdotareq.subway_e_ticketing.ui.fragment

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
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
        }

        binding.switchButton.isChecked = SharedPreferenceUtil.getSharedPrefsNightMode(context)

        binding.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                SharedPreferenceUtil.setSharedPrefsNightMode(context, true)
                Toast.makeText(context, "You should restart the app to take effect", Toast.LENGTH_LONG).show()
            } else {
                // The toggle is disabled
                SharedPreferenceUtil.setSharedPrefsNightMode(context, false)
                Toast.makeText(context, "You should restart the app to take effect", Toast.LENGTH_LONG).show()
            }
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
