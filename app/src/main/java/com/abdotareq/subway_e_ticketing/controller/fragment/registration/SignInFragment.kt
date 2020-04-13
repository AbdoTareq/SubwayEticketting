package com.abdotareq.subway_e_ticketing.controller.fragment.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

    private lateinit var viewModelFactory: SigninViewModelFactory
    private lateinit var viewModel: SigninViewModel

    private var _binding: FragmentSignInBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModelFactory = SigninViewModelFactory()

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SigninViewModel::class.java)

        binding.viewmodel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.setLifecycleOwner(this)

        // Navigates to sign up when button is pressed
        viewModel.eventSignUp.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
                viewModel.onSignUpComplete()
            }
        })

        // Navigates to recover pass when button is pressed
        viewModel.eventRecoverPass.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToForgetPassFragment())
                viewModel.onRecoverPassComplete()
            }
        })

        /* these 2 fields for observing mail and pass change if needed
         viewModel.mail.observe(viewLifecycleOwner, Observer {
             Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
         })
         viewModel.pass.observe(viewLifecycleOwner, Observer {
             Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
         })*/

        //
        viewModel.eventAuthenticate.observe(viewLifecycleOwner, Observer { isAuthenticatedClicked ->
            if (isAuthenticatedClicked) {
                validateFields()
                viewModel.onAuthenticateComplete()
            }
        })

        return view
    }

    private fun validateFields() {
        // not valid mail
        if (!viewModel.validateMail()) {
            binding.mailEt.setText("")
            binding.mailEt.hint = getString(R.string.invalid_mail_warning)
            return
        }
        // not valid pass
        if (!viewModel.validatePass()) {
            binding.passEt.setText("")
            binding.passEt.hint = getString(R.string.invalid_pass)
            return
        }
        // data is valid call authenticate
        authenticate()
//        Toast.makeText(context, "pass", Toast.LENGTH_SHORT).show()
    }

    /**
     * A method used to authenticate user (sign in)
     * can't be boolean as it has a thread which method won't wait until it finishes
     */
    private fun authenticate() {
        val user = User()

        user.email = viewModel.onGetMail()
        user.password = viewModel.onGetPass()

        Timber.e(user.toString())

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
                Toast.makeText(context, getString(R.string.check_network), Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}