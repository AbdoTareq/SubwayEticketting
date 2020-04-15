package com.abdotareq.subway_e_ticketing.ui.fragment.registration

import android.app.Activity
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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.ui.activity.HomeLandActivity
import com.abdotareq.subway_e_ticketing.databinding.FragmentSignUpBinding
import com.abdotareq.subway_e_ticketing.model.Token
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.util
import com.abdotareq.subway_e_ticketing.viewmodels.SignUpViewModelFactory
import com.abdotareq.subway_e_ticketing.viewmodels.SignupViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
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

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SignupViewModel::class.java)

        binding.viewmodel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.setLifecycleOwner(this)

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
                validateFields()
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
     * A method used to save a new user (sign up)
     */
    private fun saveUserCall(user: User?) {

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(context, getString(R.string.progMessage))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.saveUser(user)?.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299) {
                    //user saved successfully
                    progressDialog.dismiss()
                    val returnIntent = activity!!.intent
                    //write token into SharedPreferences
                    SharedPreferenceUtil.setSharedPrefsLoggedIn(context, true)
                    if (response.body() != null) {
                        SharedPreferenceUtil.setSharedPrefsTokenId(context, response.body()!!.token)
                    }
                    val intent = Intent(context, HomeLandActivity::class.java)
                    startActivity(intent)

                    activity!!.setResult(Activity.RESULT_OK, returnIntent)
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                } else if (responseCode == 434) {
                    Toast.makeText(context, getText(R.string.pass_less), Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                } else if (responseCode == 435) {
                    Toast.makeText(context, getText(R.string.mail_exist), Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                } else {
                    //user not saved successfully
                    progressDialog.dismiss()
                    Toast.makeText(context, "else onResponse $responseCode", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                progressDialog.dismiss()
                Timber.e("getText(R.string.error_message)${t.message}")
                Toast.makeText(context, getString(R.string.check_network), Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * A method called to handle sign up button clicks
     */
    private fun validateFields() {
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

        //create MobileUser object and set it's attributes
        val user = User()
        user.first_name = binding.signUpFNameEt.text.toString()
        user.last_name = binding.signUpLNameEt.text.toString()
        user.email = binding.signUpMailEt.text.toString()
        user.password = binding.signUpPassEt.text.toString()
        user.gender = binding.signUpGenderBtn.text.toString()
        user.birth_date = binding.signUpCalender.text.toString()
        user.admin = 0

        Timber.e(user.toString())

        //sign up method that will call the web service
        saveUserCall(user)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}