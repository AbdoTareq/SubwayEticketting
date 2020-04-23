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
import androidx.navigation.fragment.navArgs
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.FragmentVerificationBinding
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.NoNetworkException
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.model.Token
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.util
import com.abdotareq.subway_e_ticketing.viewmodels.VerificationViewModel
import com.abdotareq.subway_e_ticketing.viewmodels.factories.VerificationViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class VerificationFragment : Fragment() {

    private var _binding: FragmentVerificationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private lateinit var codeEt: EditText
//    private lateinit var continueBtn: Button

    private lateinit var viewModelFactory: VerificationViewModelFactory
    private lateinit var viewModel: VerificationViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        val view = binding.root

        // this how to receive with safe args
        val safeArgs: VerificationFragmentArgs by navArgs()
        val mail = safeArgs.mail

        val application = requireNotNull(activity).application

        viewModelFactory = VerificationViewModelFactory(mail, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(VerificationViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

//        codeEt = binding.verificationEt
//        continueBtn = binding.verificationContinueBtn

//        binding.verificationMailTv.text = mail

        viewModel.eventContinue.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.onContinueComplete()
                if (viewModel.validateCode())
                    verifyCode()
                else {
                    binding.verificationEt.error = getString(R.string.wrong_code)
                    binding.verificationEt.requestFocus()
                }
            }
        })


        return view
    }

    // call to verify the user
    private fun verifyCode() {

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.loading))
        progressDialog.show()

        val registerInterface = object : RegisterInterface {
            override fun onSuccess(token: String) {
                progressDialog.dismiss()
                // this how to navigate between fragments using safe args after defining action in navigation xml file
                // between desired fragments & send args safely
                findNavController().navigate(VerificationFragmentDirections
                        .actionVerificationFragmentToChangePassFragment(viewModel.mail.value!!, token))
            }

            override fun onFail(responseCode: Int) {
                progressDialog.dismiss()
                Toast.makeText(context, getErrorMessage(responseCode, activity!!.application), Toast.LENGTH_LONG).show()
            }
        }
        viewModel.verifyCode(registerInterface)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
