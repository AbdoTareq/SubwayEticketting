package com.abdotareq.subway_e_ticketing.ui.fragment.registration

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.ui.activity.HomeLandActivity
import com.abdotareq.subway_e_ticketing.databinding.FragmentSignUpBinding
import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.util
import com.abdotareq.subway_e_ticketing.viewmodels.SignUpViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.SignupViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * The Activity Controller Class that is responsible for handling Signing Up
 */

class SignUpFragment : Fragment() {

    private lateinit var viewModelFactory: SignUpViewModelFactory
    private lateinit var viewModel: SignupViewModel

    private var _binding: FragmentSignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var genderList = arrayOf("Female", "Male")
    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var gender = ""
    private var birthDate: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModelFactory = SignUpViewModelFactory()

        viewModel = ViewModelProvider(this, viewModelFactory).get(SignupViewModel::class.java)

        binding.viewmodel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        /* viewModel.first.observe(viewLifecycleOwner, Observer {
             Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
         })

         viewModel.last.observe(viewLifecycleOwner, Observer {
             Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
         })

         viewModel.mail.observe(viewLifecycleOwner, Observer {
             Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
         })
 */

        viewModel.eventSignIn.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            viewModel.onSignInComplete()
        })

        viewModel.eventRegister.observe(viewLifecycleOwner, Observer { signUpClicked ->
            if (signUpClicked) {
                signUpBtnClick()
                viewModel.onRegisterComplete()
            }
        })

        // code goes here
        callListeners()

        return view
    }

    // Call listeners on the activity for code readability
    private fun callListeners() {
        binding.signUpGenderBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle(getString(R.string.select_gender))
            builder.setItems(genderList) { dialogInterface, position ->
                gender = genderList[position]
                binding.signUpGenderBtn.text = gender
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
        binding.signUpCalender.setOnClickListener {
            val datePickerDialog = DatePickerDialog(context!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//                        birthDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                        val calendar = Calendar.getInstance()
                        calendar[Calendar.YEAR] = year
                        calendar[Calendar.MONTH] = monthOfYear
                        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val currentDateString: String = dateFormat.format(calendar.getTime())
                        binding.signUpCalender.text = currentDateString
                        birthDate = currentDateString

                    }, mYear, mMonth, mDay)
            datePickerDialog.show()
        }
    }

    /**
     * A method called to handle sign up button clicks
     */
    private fun signUpBtnClick() {
        //check for all inputs from user are correct
        // not valid mail
        if (viewModel.first.value.isNullOrEmpty()) {
            binding.signUpFNameEt.setText("")
            binding.signUpFNameEt.hint = getString(R.string.first_name_warrning)
            return
        } else if (viewModel.last.value.isNullOrEmpty()) {
            binding.signUpFNameEt.setText("")
            binding.signUpFNameEt.hint = getString(R.string.fix_last_name_mess)
            return
        } else if (!viewModel.validateMail()) {
            binding.signUpMailEt.setText("")
            binding.signUpMailEt.hint = getString(R.string.invalid_mail_warning)
            return
        } else if (!viewModel.validatePass(viewModel.pass.value.toString())) {
            binding.signUpPassEt.setText("")
            binding.signUpPassEt.hint = getString(R.string.invalid_pass)
            return
        } else if (!viewModel.validatePass(viewModel.confPass.value.toString())) {
            binding.signUpConfirmPassEt.setText("")
            binding.signUpConfirmPassEt.hint = getString(R.string.invalid_pass)
            return
        } else if (viewModel.gender.value == "Gender") {
            Toast.makeText(context, getString(R.string.select_gender), Toast.LENGTH_LONG).show()
            return
        } else if (viewModel.birthDate.value == "Birth Date") {
            Toast.makeText(context, getString(R.string.select_birthday), Toast.LENGTH_LONG).show()
            return
        }

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        val registerInterface = object : RegisterInterface {
            override fun onSuccess(token: String) {
                //user authenticated successfully

                //write token into SharedPreferences to use in remember user
                SharedPreferenceUtil.setSharedPrefsLoggedIn(context, true)
                SharedPreferenceUtil.setSharedPrefsTokenId(context, token)
                progressDialog.dismiss()
                val intent = Intent(context, HomeLandActivity::class.java)
                startActivity(intent)
                activity!!.finishAffinity()
            }

            override fun onFail(responseCode: Int) {
                progressDialog.dismiss()
                when (responseCode) {
                    -1 -> Toast.makeText(context, getString(R.string.check_network), Toast.LENGTH_LONG).show()
                    434 -> Toast.makeText(context, getText(R.string.pass_less), Toast.LENGTH_LONG).show()
                    435 -> Toast.makeText(context, getText(R.string.mail_exist), Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(context, "else onResponse", Toast.LENGTH_LONG).show()
                }
            }
        }

        //sign up method that will call the web service
        viewModel.saveUserCall(registerInterface)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}