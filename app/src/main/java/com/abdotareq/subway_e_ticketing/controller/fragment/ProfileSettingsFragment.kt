package com.abdotareq.subway_e_ticketing.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentProfileSettingsBinding
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.imageUtil.BitmapConverter
import com.abdotareq.subway_e_ticketing.utility.util
import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.model.ConfigSplash
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

/**
 *  [ProfileSettingsFragment] responsible for user profile settings & changes.
 */
class ProfileSettingsFragment : Fragment() {

    private var _binding: FragmentProfileSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        getUserData()

        binding.changePassBtn.setOnClickListener {
            openDialog()
        }

        return view
    }

    private fun getUserData() {
        // receive user obj from splash screen
        try {
            user = activity?.intent?.getSerializableExtra("user") as User
            showData()
        } catch (e: Exception) {
            Timber.e("$e")
            // if failed to get user obj from splash screen get user call
            userCall()
        }


    }

    private fun userCall() {
        if (SharedPreferenceUtil.getSharedPrefsLoggedIn(context)) {
            getUserData(SharedPreferenceUtil.getSharedPrefsTokenId(context))
        }
    }

    private fun getUserData(userIdToken: String) {
        var bearerToken = "Bearer "
        bearerToken += userIdToken

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.getUser(bearerToken).enqueue(object : retrofit2.Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //get user successfully
                    user = response.body()!!
                    progressDialog.dismiss()
                    showData()

                } else if (responseCode == 438) {
                    //pass not saved successfully
                    Toast.makeText(context, getString(R.string.pass_less), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.user_not_found))
                    progressDialog.dismiss()
                } else {
                    //user not saved successfully
                    Toast.makeText(context, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.else_on_repsonse))
                    progressDialog.dismiss()

                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(context, getString(R.string.failure_happened), Toast.LENGTH_LONG).show()
                Timber.e("$t")
                progressDialog.dismiss()
            }
        })
    }


    // set profile fields
    private fun showData() {

        try {
            binding.firstNameEt.setText(user?.first_name)
            binding.lastNameEt.setText(user?.last_name)
            binding.mail.setText(user?.email)

            //if image exists
            if (user!!.image != null) {
                val bitMapCon = BitmapConverter(BitmapConverter.AsyncResponse {
                    binding.profileImage.setImageBitmap(it)
                })
                bitMapCon.execute(user?.image)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // this for change pass dialog
    private fun openDialog() {
        val passDialog = ChangePassDialogFragment(binding.mail.text.toString())
        fragmentManager?.let { passDialog.show(it, "Pass Dialog") }
    }


}
