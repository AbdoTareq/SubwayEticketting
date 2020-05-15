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
import com.abdotareq.subway_e_ticketing.databinding.FragmentChangePassBinding
import com.abdotareq.subway_e_ticketing.data.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.data.model.UserInterface
import com.abdotareq.subway_e_ticketing.utility.Util
import com.abdotareq.subway_e_ticketing.viewmodels.factories.ViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.register.ChangePassViewModel

class ChangePassFragment : Fragment() {

    private var _binding: FragmentChangePassBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ChangePassViewModel

    private var bearerToken: String = "Bearer "

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangePassBinding.inflate(inflater, container, false)
        val view = binding.root

        // take the token to send it with bearer in header to change pass
        val safeArgs: ChangePassFragmentArgs by navArgs()
        val token = safeArgs.token
        val mail = safeArgs.mail

        // take the token to send it with bearer in header to change pass
        bearerToken += token

        val application = requireNotNull(activity).application

        viewModelFactory = ViewModelFactory(application, mail, token)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ChangePassViewModel::class.java)

        binding.viewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        viewModel.eventChangePass.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.onChangePassComplete()
                confirmBtnClick()
            }
        })

        return view
    }

    /**
     * A method called to handle sign up button clicks
     */
    private fun confirmBtnClick() {
        //check for all inputs from user are correct
        if (!Util.isValidPassword(viewModel.pass.value)) {
            binding.passTi.error = getString(R.string.invalid_pass)
            binding.passTi.requestFocus()
            return
        } else if (viewModel.confirmPass.value!!.isEmpty()
                || viewModel.confirmPass.value!! != viewModel.pass.value!!) {
            binding.confirmTi.error = getString(R.string.fix_confirmPassWarning)
            binding.confirmTi.requestFocus()
            return
        }

        //initialize and show a progress dialog to the user
        val progressDialog = Util.initProgress(context, getString(R.string.loading))
        progressDialog.show()

        val userInterface = object : UserInterface {
            override fun onSuccess() {
                //pass changed successfully
                progressDialog.dismiss()
                findNavController().navigate(ChangePassFragmentDirections.actionChangePassFragmentToSignInFragment())
                Toast.makeText(context, "Pass changed", Toast.LENGTH_LONG).show()
            }

            override fun onFail(responseCode: String) {
                progressDialog.dismiss()
                Toast.makeText(context, getErrorMessage(responseCode, activity!!.application), Toast.LENGTH_LONG).show()
            }

        }

        viewModel.changePass(userInterface)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}