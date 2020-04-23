package com.abdotareq.subway_e_ticketing.ui.fragment.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentVerificationBinding
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.NoNetworkException
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.Token
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class VerificationFragment : Fragment() {

    private var _binding: FragmentVerificationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mailTv: TextView
    private lateinit var codeEt: EditText
    private lateinit var continueBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        val view = binding.root


        // this how to receive with safe args
        val safeArgs: VerificationFragmentArgs by navArgs()
        val mail = safeArgs.mail

        mailTv = binding.verificationMailTv
        codeEt = binding.verificationEt
        continueBtn = binding.verificationContinueBtn

        mailTv.text = mail

        continueBtn.setOnClickListener {
            if (codeEt.text.isNullOrEmpty()) {
                codeEt.hint = getText(R.string.enter_verification)
                codeEt.setHintTextColor(getColor(context!!, R.color.primaryTextColor))
            } else {
                verifyCode(mail, codeEt.text.toString())
            }
        }
        return view
    }

    // call to verify the user
    private fun verifyCode(mail: String, code: String) {
        val user = User()
        user.email = mail
        user.otp_token = code

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.loading))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.verifyCodeCall(user)?.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //verifyCode successfully
                    progressDialog.dismiss()
                    //ToDo: Remove Toast

                    Toast.makeText(context, "token: " + response.body()!!.token, Toast.LENGTH_SHORT).show()
                    // this how to navigate between fragments using safe args after defining action in navigation xml file
                    // between desired fragments & send args safely
                    findNavController().navigate(VerificationFragmentDirections.actionVerificationFragmentToChangePassFragment(mail, response.body()!!.token))

                } else {
                    //verifyCode not successfully
                    progressDialog.dismiss()
                    Timber.e("response.code:    $responseCode")

                    Toast.makeText(context, getErrorMessage(responseCode, activity!!.application), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                Timber.e("err:    $t")
                progressDialog.dismiss()
                Toast.makeText(context, getErrorMessage(NoNetworkException, activity!!.application), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
