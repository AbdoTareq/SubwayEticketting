package com.abdotareq.subway_e_ticketing.controller.fragment.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.controller.activity.HomeLandActivity
import com.abdotareq.subway_e_ticketing.databinding.FragmentSignInBinding
import com.abdotareq.subway_e_ticketing.model.dto.Token
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * The Activity Controller Class that is responsible for handling Signing in
 */
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var passEt: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        // code goes here
        callListeners()
        
        return view
    }

    // Call listeners on the activity for code readability
    private fun callListeners() {
        val signInBtn = binding.signInBtn
        val signUpTv = binding.signInSignUpTv
        val recoverPassTv = binding.signInRecoverPassTv
        signInBtn.setOnClickListener { signInBtnClick() }
        signUpTv.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
        recoverPassTv.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToForgetPassFragment())
        }
    }

    // Handle sign in Button clicks
    private fun signInBtnClick() {
        val mailEt = binding.signInMailEt
        passEt = binding.signInPassEt

        mailEt.setText("abdo.elbishihi@gmail.com")
        passEt.setText("abdo1234")
        Toast.makeText(context, "Const values written in signInBtnClick method ", Toast.LENGTH_LONG).show()

        //check for all inputs from user are not empty
        if (util.isValidEmail(mailEt.text.toString())) {
            mailEt.setText("")
            mailEt.hint = getString(R.string.invalid_mail_warning)
            return
        } else if (!util.isValidPassword(passEt.text.toString())) {
            passEt.setText("")
            passEt.hint = getString(R.string.pass_warning)
            return
        }
        val user = User()
        user.email = mailEt.text.toString()
        user.password = passEt.text.toString()
        Timber.e(user.toString())

        authenticate(user)
    }

    /**
     * A method used to authenticate user (sign in)
     * can't be boolean as it has a thread which method won't wait until it finishes
     */
    private fun authenticate(user: User) {

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.authenticate(user)?.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //user authenticated successfully
                    progressDialog.dismiss()

                    //write token into SharedPreferences to use in remember user
                    SharedPreferenceUtil.setSharedPrefsLoggedIn(context, true)
                    SharedPreferenceUtil.setSharedPrefsTokenId(context, response.body()!!.token)
                    Timber.e("token:    ${response.body()!!.token}")

                    val intent = Intent(context, HomeLandActivity::class.java)
                    startActivity(intent)
                    activity!!.finishAffinity()
                } else if (responseCode == 436) {
                    //user not authenticated successfully
                    progressDialog.dismiss()
                    Toast.makeText(context, getText(R.string.wrong_mail_or_pass), Toast.LENGTH_LONG).show()
                } else {
                    //user not authenticated successfully
                    progressDialog.dismiss()
                    Toast.makeText(context, "else onResponse", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                progressDialog.dismiss()
                Timber.e("getText(R.string.error_message)${t.message}")
                Toast.makeText(context, getText(R.string.failure_happened), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}