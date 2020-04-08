package com.abdotareq.subway_e_ticketing.ui.activity.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.ActivityForgetPassBinding
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.util
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ForgetPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPassBinding
    private lateinit var mailEt: EditText
    private lateinit var sendCode: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        // this for view binding to replace findviewbyid
        binding = ActivityForgetPassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mailEt = binding.forgetPassMailEt
        sendCode = binding.forgetPassSendVerificationBtn
        sendCode.setOnClickListener(View.OnClickListener {
            if (util.isValidEmail(mailEt.text.toString())) {
                mailEt.setText("")
                mailEt.hint = getString(R.string.invalid_mail_warning)
                return@OnClickListener
            } else sendVerificationCode(mailEt.text.toString())
        })
    }

    // send send Verification Code to user mail
    private fun sendVerificationCode(mail: String) {
        val user = User(mail)

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(this@ForgetPassActivity, getString(R.string.loading))
        progressDialog.show()

        //start the call
        UserApiObj.retrofitService.sendVerificationCode(user)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //user saved successfully
                    progressDialog.dismiss()
                    //ToDo: Remove Toast
                    val intent = Intent(this@ForgetPassActivity, VerificationActivity::class.java)
                    intent.putExtra("mail", mail)
                    startActivity(intent)
                    finish()
                } else {
                    //user not saved successfully
                    progressDialog.dismiss()
                    Toast.makeText(this@ForgetPassActivity, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@ForgetPassActivity, getString(R.string.error_message), Toast.LENGTH_LONG).show()
                //                //ToDo: Remove Toast
                Timber.e("FAILED : ${ t.message}" )
            }
        })
    }
}