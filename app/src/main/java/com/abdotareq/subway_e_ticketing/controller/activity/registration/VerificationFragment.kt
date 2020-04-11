package com.abdotareq.subway_e_ticketing.controller.activity.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentVerificationBinding
import com.abdotareq.subway_e_ticketing.model.dto.Token
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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


        val mail = activity!!.intent.getStringExtra("mail")

        mailTv = binding.verificationMailTv
        codeEt = binding.verificationEt
        continueBtn = binding.verificationContinueBtn

        mailTv.text = mail

        continueBtn.setOnClickListener {
            if (codeEt.text.isNullOrEmpty()) {
                codeEt.hint = getText(R.string.enter_verification)
                codeEt.setHintTextColor(resources.getColor(R.color.colorAccent))
            } else {
                verifyCode(mail, codeEt.text.toString())
            }
        }
        return view
    }

    // call to verify the user
    private fun verifyCode(mail: String?, code: String) {
        val user = User()
        user.email = mail
        user.otp_token = code

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.loading))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.verifyCode(user)?.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //verifyCode successfully
                    progressDialog.dismiss()
                    //ToDo: Remove Toast
                    activity!!.intent.putExtra("token", response.body()!!.token)
                    activity!!.intent.putExtra("mail", mail)
                    Toast.makeText(context, "token: " + response.body()!!.token, Toast.LENGTH_SHORT).show()
                    // this how to navigate between fragments using safe args after defining action in navigation xml file
                    // between desired fragments
                    findNavController().navigate(VerificationFragmentDirections.actionVerificationFragmentToChangePassFragment())
                } else if (responseCode == 439) {
                    //verifyCode not successfully
                    progressDialog.dismiss()
                    Toast.makeText(context, getString(R.string.wrong_code), Toast.LENGTH_LONG).show()
                } else {
                    //verifyCode not successfully
                    progressDialog.dismiss()
                    Toast.makeText(context, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(context, getString(R.string.failure_happened), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}