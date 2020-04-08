package com.abdotareq.subway_e_ticketing.ui.activity.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.ActivityVerificationBinding
import com.abdotareq.subway_e_ticketing.model.dto.Token
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding

    private lateinit var mailTv: TextView
    private lateinit var codeEt: EditText
    private lateinit var continueBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        // this for view binding to replace findviewbyid
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mail = intent.getStringExtra("mail")

        mailTv = binding.verificationMailTv
        codeEt = binding.verificationEt
        continueBtn = binding.verificationContinueBtn

        mailTv.text = mail

        continueBtn.setOnClickListener {
            if (codeEt.text.isNullOrEmpty()) {
                codeEt.hint = getText(R.string.enter_verification)
                codeEt.setHintTextColor(resources.getColor(R.color.colorAccent))
            } else {
                verifyCode(mail, codeEt.text.toString())
            }
        }

    }

    // call to verify the user
    private fun verifyCode(mail: String, code: String) {
        val user = User(mail, code)

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(this@VerificationActivity, getString(R.string.loading))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.verifyCode(user)?.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //verifyCode successfully
                    progressDialog.dismiss()
                    //ToDo: Remove Toast
                    val intent = Intent(this@VerificationActivity, ChangePassActivity::class.java)
                    intent.putExtra("token", response.body()!!.token)
                    intent.putExtra("mail", mail)
                    Toast.makeText(this@VerificationActivity, "token: " + response.body()!!.token, Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                } else if (responseCode == 439) {
                    //verifyCode not successfully
                    progressDialog.dismiss()
                    Toast.makeText(this@VerificationActivity, getString(R.string.wrong_code), Toast.LENGTH_LONG).show()
                }else {
                    //verifyCode not successfully
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