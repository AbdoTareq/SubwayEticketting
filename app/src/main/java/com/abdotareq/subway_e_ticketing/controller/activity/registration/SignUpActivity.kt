package com.abdotareq.subway_e_ticketing.controller.activity.registration

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.ActivitySignUpBinding
import com.abdotareq.subway_e_ticketing.model.dto.Token
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.controller.activity.HomeLandActivity
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * The Activity Controller Class that is responsible for handling Signing Up
 */
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var genderList = arrayOf("Female", "Male")
    private lateinit var materialCalendar: Calendar
    private lateinit var datePicker: DatePickerDialog
    private lateinit var signUpBtn: Button
    private lateinit var calenderBtn: Button
    private lateinit var genderBtn: Button
    private lateinit var signInTv: TextView
    private var year = 0
    private var date: Date? = null
    private var formatDate: String? = null
    private var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        callListeners()
    }

    // Call listeners on the activity for code readability
    private fun callListeners() {
        genderBtn = binding.signUpGenderBtn
        calenderBtn = binding.signUpCalender
        signUpBtn = binding.signUpBtn
        signInTv = binding.signUpSignInTv

        genderBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this@SignUpActivity)
            builder.setTitle(getString(R.string.select_gender))
            builder.setItems(genderList) { dialogInterface, position ->
                gender = genderList[position]
                genderBtn.text = gender
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
        calenderBtn.setOnClickListener {
            materialCalendar = Calendar.getInstance()
            val day = materialCalendar.get(Calendar.DAY_OF_MONTH)
            val month = materialCalendar.get(Calendar.MONTH)
            year = materialCalendar.get(Calendar.YEAR)
            date = materialCalendar.getTime()
            val format1 = SimpleDateFormat("yyyy-MM-dd")
            formatDate = format1.format(date)
            datePicker = DatePickerDialog(this@SignUpActivity, OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                calenderBtn.text = formatDate
            }, year, month, day) //changed from day,month,year to year,month,day
            datePicker.show()
        }

        //sign up method that will call the web service
        signUpBtn.setOnClickListener { signUpBtnClick() }
        signInTv.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * A method used to save a new user (sign up)
     */
    private fun saveUserCall(user: User?) {

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(this, getString(R.string.progMessage))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.saveUser(user)?.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299) {
                    //user saved successfully
                    progressDialog.dismiss()
                    val returnIntent = intent
                    //write token into SharedPreferences
                    SharedPreferenceUtil.setSharedPrefsLoggedIn(this@SignUpActivity, true)
                    if (response.body() != null) {
                        SharedPreferenceUtil.setSharedPrefsTokenId(this@SignUpActivity, response.body()!!.token)
                    }
                    val intent = Intent(this@SignUpActivity, HomeLandActivity::class.java)
                    startActivity(intent)

                    setResult(Activity.RESULT_OK, returnIntent)
                    Toast.makeText(this@SignUpActivity, "success", Toast.LENGTH_SHORT).show()
                } else if (responseCode == 434) {
                    Toast.makeText(this@SignUpActivity, getText(R.string.pass_less), Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                } else if (responseCode == 435) {
                    Toast.makeText(this@SignUpActivity, getText(R.string.mail_exist), Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                } else {
                    //user not saved successfully
                    progressDialog.dismiss()
                    Toast.makeText(this@SignUpActivity, "else onResponse $responseCode", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                progressDialog.dismiss()
                Timber.e("getText(R.string.error_message)${t.message}")
                Toast.makeText(this@SignUpActivity, getString(R.string.failure_happened), Toast.LENGTH_LONG).show()
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
            binding.signUpPassEt.hint = getString(R.string.pass_warning)
            return
        } else if (binding.signUpPassEt.text.toString() != binding.signUpConfirmPassEt.text.toString()) {
            binding.signUpConfirmPassEt.setText("")
            binding.signUpConfirmPassEt.hint = getString(R.string.fix_confirmPassWarning)
            return
        } else if (gender.isEmpty()) {
            Toast.makeText(this, getText(R.string.select_gender), Toast.LENGTH_SHORT).show()
            return
        } else if (year == 0) {
            Toast.makeText(this, getText(R.string.select_birthday), Toast.LENGTH_SHORT).show()
            return
        }

        //create MobileUser object and set it's attributes
        val user = User()
        user.first_name = binding.signUpFNameEt.text.toString()
        user.last_name = binding.signUpLNameEt.text.toString()
        user.email = binding.signUpMailEt.text.toString()
        user.password = binding.signUpPassEt.text.toString()
        user.gender = gender
        user.birth_date = formatDate
        user.admin = 0

        Timber.e(user.toString())

        //sign up method that will call the web service
        saveUserCall(user)
    }
}