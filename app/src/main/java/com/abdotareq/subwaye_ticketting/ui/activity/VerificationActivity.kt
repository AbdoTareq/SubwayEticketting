package com.abdotareq.subwaye_ticketting.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subwaye_ticketting.R
import com.abdotareq.subwaye_ticketting.databinding.ActivityVerificationBinding
import com.abdotareq.subwaye_ticketting.model.dto.Token
import com.abdotareq.subwaye_ticketting.model.dto.User
import com.abdotareq.subwaye_ticketting.model.retrofit.UserService
import com.abdotareq.subwaye_ticketting.utility.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding

    private lateinit var mailTv: TextView
    private lateinit var codeEt: EditText
    private lateinit var continueBtn: Button
    private lateinit var retrofit: Retrofit

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        val mail = intent.getStringExtra("mail")

        // this for view binding to replace findviewbyid
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //initialize retrofit object
        retrofit = Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        mailTv = binding.verificationMailTv
        codeEt = binding.verificationEt
        continueBtn = binding.verificationContinueBtn
        mailTv.text = mail

        continueBtn.setOnClickListener(View.OnClickListener {
            if (codeEt.text.isNullOrEmpty()) {
                codeEt.hint = getText(R.string.enter_verification)
                codeEt.setHintTextColor(getColor(R.color.colorAccent))
            } else {
                verifyCode(mail, codeEt.text.toString())
            }
        })
    }

    private fun verifyCode(mail: String, code: String) {
        val user = User(mail, code)

        //create UserService object
        val userService = retrofit.create(UserService::class.java)

        //initialize the save user call
        val call = userService.verifyCode(user)

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(this@VerificationActivity, getString(R.string.loading))
        progressDialog.show()

        //start the call
        call.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //user saved successfully
                    progressDialog.dismiss()
                    //ToDo: Remove Toast
                    val intent = Intent(this@VerificationActivity, ChangePassActivity::class.java)
                    intent.putExtra("token", response.body()!!.token)
                    Toast.makeText(this@VerificationActivity, "token: " + response.body()!!.token, Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                } else {
                    //user not saved successfully
                    progressDialog.dismiss()
                    Toast.makeText(this@VerificationActivity, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@VerificationActivity, getString(R.string.error_message), Toast.LENGTH_LONG).show()

            }
        })
    }
}