package com.abdotareq.subway_e_ticketing.ui.fragment.registration

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
import com.abdotareq.subway_e_ticketing.databinding.FragmentForgetPassBinding
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.UserInterface
import com.abdotareq.subway_e_ticketing.utility.util
import com.abdotareq.subway_e_ticketing.viewmodels.register.ForgetPassViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.ForgetPassViewModelFactory

/**
 *  [ForgetPassFragment] responsible for sending verification code to user mail
 *  to retrieve his pass
 */
class ForgetPassFragment : Fragment() {

    private var _binding: FragmentForgetPassBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ForgetPassViewModelFactory
    private lateinit var viewModel: ForgetPassViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentForgetPassBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(activity).application

        viewModelFactory = ForgetPassViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ForgetPassViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        viewModel.eventSendCode.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.onSendCodeComplete()
                sendVerificationCode(binding.forgetPassMailEt.text.toString())
            }
        })

        return view
    }

    // send send Verification Code to user mail
    private fun sendVerificationCode(mail: String) {
        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.loading))

        val userInterface: UserInterface = object : UserInterface {
            override fun onSuccess() {
                progressDialog.dismiss()
                // this how to navigate between fragments using safe args after defining action in navigation xml file
                // between desired fragments
                findNavController().navigate(ForgetPassFragmentDirections.actionForgetPassFragmentToVerificationFragment(mail))
            }

            override fun onFail(responseCode: Int) {
                progressDialog.dismiss()
                Toast.makeText(context, getErrorMessage(responseCode, activity!!.application), Toast.LENGTH_LONG).show()
            }
        }
        //start the call
        if (viewModel.validateMail()) {
            progressDialog.show()
            viewModel.forgetPass(userInterface)
        } else {
            binding.mailTi.error = getString(R.string.invalid_mail_warning)
            binding.mailTi.requestFocus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}