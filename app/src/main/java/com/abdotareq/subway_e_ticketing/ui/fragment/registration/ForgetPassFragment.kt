package com.abdotareq.subway_e_ticketing.ui.fragment.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentForgetPassBinding
import com.abdotareq.subway_e_ticketing.model.ErrorStatus
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.util
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 *  [ForgetPassFragment] responsible for sending verification code to user mail
 *  to retrieve his pass
 */
class ForgetPassFragment : Fragment() {

    private var _binding: FragmentForgetPassBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentForgetPassBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.forgetPassSendVerificationBtn.setOnClickListener {
            if (!util.isValidEmail(binding.forgetPassMailEt.text.toString()))
                sendVerificationCode(binding.forgetPassMailEt.text.toString())
            else {
                binding.forgetPassMailEt.error = getString(R.string.invalid_mail_warning)
                binding.forgetPassMailEt.requestFocus()
            }
        }

        return view
    }

    // send send Verification Code to user mail
    private fun sendVerificationCode(mail: String) {
        val user = User()
        user.email = mail
        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.loading))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.sendVerificationCodeCall(user)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //user saved successfully
                    progressDialog.dismiss()
                    // this how to navigate between fragments using safe args after defining action in navigation xml file
                    // between desired fragments
                    findNavController().navigate(ForgetPassFragmentDirections.actionForgetPassFragmentToVerificationFragment(mail))
                } else {
                    //user not saved successfully
                    progressDialog.dismiss()
                    Toast.makeText(context, getErrorMessage(responseCode, activity!!.application), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(context, getErrorMessage(ErrorStatus.Codes.NoNetworkException, activity!!.application), Toast.LENGTH_LONG).show()
                //                //ToDo: Remove Toast
                Timber.e("FAILED : ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}