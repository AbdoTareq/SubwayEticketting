package com.abdotareq.subway_e_ticketing.ui.fragment.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.data.model.ErrorStatus
import com.abdotareq.subway_e_ticketing.data.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.databinding.FragmentSignInBinding
import com.abdotareq.subway_e_ticketing.ui.activity.HomeLandActivity
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil.setSharedPrefsLoggedIn
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil.setSharedPrefsTokenId
import com.abdotareq.subway_e_ticketing.utility.Util
import com.abdotareq.subway_e_ticketing.viewmodels.factories.ViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.register.SigninViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import timber.log.Timber

/**
 * The Activity Controller Class that is responsible for handling Signing in
 */
class SignInFragment : Fragment() {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SigninViewModel

    private var _binding: FragmentSignInBinding? = null

    private val RC_SIGN_IN: Int = 1

    private lateinit var mGoogleSignInClient: GoogleSignInClient


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        viewModelFactory = ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(SigninViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

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
        viewModel.eventAuthenticate.observe(viewLifecycleOwner, Observer { isAuthenticatedClicked ->
            if (isAuthenticatedClicked) {
                validateFields()
                viewModel.onAuthenticateComplete()
            }
        })


        // When you upload app on play store After signing , it will give you a new SHA
        // which you have to add in your firebase account. then it'll work InSahaAllah
        val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken("9451048394-t24jeq5dd122j0kjfttu8art14p4j3ds.apps.googleusercontent.com")
                        .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.signInButton.setSize(SignInButton.SIZE_WIDE)

        binding.signInButton.setOnClickListener {
            googleSignIn()
        }

        return view
    }

    private fun validateFields() {
        // not valid mail
        if (!viewModel.validateMail()) {
            binding.mailEt.error = getString(R.string.invalid_mail_warning)
            binding.mailEt.requestFocus()
            return
        }
        // not valid pass
        if (!viewModel.validatePass()) {
            binding.passTi.error = getString(R.string.invalid_pass)
            binding.passTi.requestFocus()
            return
        }
        // data is valid call authenticate
        authenticate()

    }

    /**
     * A method used to authenticate user (sign in)
     * can't be boolean as it has a thread which method won't wait until it finishes
     */
    private fun authenticate() {
        //initialize and show a progress dialog to the user
        val progressDialog = Util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        val registerInterface = object : RegisterInterface {
            override fun onSuccess(token: String) {
                //write token into SharedPreferences to use in remember user
                setSharedPrefsLoggedIn(context, true)
                setSharedPrefsTokenId(context, token)
                progressDialog.dismiss()
                val intent = Intent(context, HomeLandActivity::class.java)
                startActivity(intent)
                activity!!.finishAffinity()
            }

            override fun onFail(responseCode: String) {
                progressDialog.dismiss()
                Toast.makeText(context, viewModel.getErrorMess(responseCode), Toast.LENGTH_LONG).show()
            }
        }

        viewModel.authenticateCall(registerInterface)

    }

    private fun googleSignIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        //initialize and show a progress dialog to the user
        val progressDialog = Util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()
        try {
            val account =
                    completedTask.getResult(ApiException::class.java)

            val registerInterface = object : RegisterInterface {
                override fun onSuccess(token: String) {
                    progressDialog.dismiss()
                    //write token into SharedPreferences to use in remember user
                    setSharedPrefsLoggedIn(context, true)
                    setSharedPrefsTokenId(context, token)
                    val intent = Intent(context, HomeLandActivity::class.java)
                    startActivity(intent)
                    activity!!.finishAffinity()
                }

                override fun onFail(responseCode: String) {
                    progressDialog.dismiss()
                    if (responseCode == ErrorStatus.Codes.UserNotFound)
                        findNavController().navigate(SignInFragmentDirections
                                .actionSignInFragmentToGoogleRegisterFragment(account!!.idToken!!))
                    Toast.makeText(context, viewModel.getErrorMess(responseCode), Toast.LENGTH_LONG).show()
                }
            }

            // Signed in successfully, show authenticated UI.
            viewModel.authenticateGoogle(account!!.idToken!!, registerInterface)

        } catch (e: ApiException) {
            progressDialog.dismiss()
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.e("signInFailed code= $e.statusCode")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}