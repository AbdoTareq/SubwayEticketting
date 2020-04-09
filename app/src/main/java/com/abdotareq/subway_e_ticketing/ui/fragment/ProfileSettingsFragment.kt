package com.abdotareq.subway_e_ticketing.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.dto.User
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ProfileSettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =inflater.inflate(R.layout.fragment_profile_settings, container, false)

        // receive user obj from splash screen
        val user: User = activity?.intent?.getSerializableExtra("user") as User
        user.let { // means if not null or empty
            Timber.e("$user")
        }

        return v
    }

}
