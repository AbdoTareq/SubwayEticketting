package com.abdotareq.subway_e_ticketing.controller.fragment.registration

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.controller.activity.HomeLandActivity
import com.abdotareq.subway_e_ticketing.databinding.FragmentSignUpBinding
import com.abdotareq.subway_e_ticketing.model.dto.Token
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

/**
 * The Activity Controller Class that is responsible for handling Signing Up
 */

class SignUpFragment : Fragment() {

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

        // code goes here
        callListeners()

        return view
    }

    // Call listeners on the activity for code readability
    private fun callListeners() {

        binding.signUpGenderBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_gender))
            builder.setItems(genderList) { dialogInterface, position ->
                gender = genderList[position]
                binding.signUpGenderBtn.text = gender
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
        binding.signUpCalender.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c[Calendar.YEAR]
            mMonth = c[Calendar.MONTH]
            mDay = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(context!!,
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        binding.signUpCalender.text = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                        birthDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                    }, mYear, mMonth, mDay)
            datePickerDialog.show()
        }

        //sign up method that will call the web service
        binding.signUpBtn.setOnClickListener { signUpBtnClick() }
        binding.signUpSignInTv.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
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
    private fun signUpBtnClick() {
        //check for all inputs from user are correct
        if (TextUtils.isEmpty(binding.signUpFNameEt.text.toString()) && binding.signUpFNameEt.text.toString() == "") {
            binding.signUpFNameEt.setText(getText(R.string.fix_fist_name))
            return
        } else if (TextUtils.isEmpty(binding.signUpLNameEt.text.toString()) && binding.signUpLNameEt.text.toString() == "") {
            binding.signUpLNameEt.setText(getText(R.string.fix_last_name_mess))
            return
        } else if (util.isValidEmail(binding.signUpMailEt.text.toString())) {
            binding.signUpMailEt.setText(getString(R.string.invalid_mail_warning))
            return
        } else if (!util.isValidPassword(binding.signUpPassEt.text.toString())) {
            binding.signUpPassEt.setText("")
            binding.signUpPassEt.hint = getString(R.string.invalid_pass)
            return
        } else if (binding.signUpPassEt.text.toString() != binding.signUpConfirmPassEt.text.toString()) {
            binding.signUpConfirmPassEt.setText("")
            binding.signUpConfirmPassEt.hint = getString(R.string.fix_confirmPassWarning)
            return
        } else if (gender.isEmpty()) {
            Toast.makeText(context, getText(R.string.select_gender), Toast.LENGTH_SHORT).show()
            return
        } else if (birthDate == null) {
            Toast.makeText(context, getText(R.string.select_birthday), Toast.LENGTH_SHORT).show()
            return
        }

        //create MobileUser object and set it's attributes
        val user = User()
        user.first_name = binding.signUpFNameEt.text.toString()
        user.last_name = binding.signUpLNameEt.text.toString()
        user.email = binding.signUpMailEt.text.toString()
        user.password = binding.signUpPassEt.text.toString()
        user.gender = gender
        user.birth_date = birthDate
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