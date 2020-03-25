package com.abdotareq.subwaye_ticketting.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subwaye_ticketting.R
import com.abdotareq.subwaye_ticketting.databinding.ActivityChangePassBinding
import com.abdotareq.subwaye_ticketting.model.dto.Token
import com.abdotareq.subwaye_ticketting.model.dto.User
import com.abdotareq.subwaye_ticketting.model.retrofit.UserService
import com.abdotareq.subwaye_ticketting.utility.util
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.security.auth.callback.Callback

class ChangePassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePassBinding

    private lateinit var passEt: EditText
    private lateinit var confirmEt: EditText
    private lateinit var confirmBtn: Button

    private lateinit var retrofit: Retrofit

    private lateinit var mail: String
    private lateinit var token: String
    private var bearerToken: String = "Bearer "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)

        // this for view binding to replace findviewbyid
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //initialize retrofit object
        retrofit = Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // take the token to send it with bearer in header to change pass
        token = intent.getStringExtra("token")
        mail = intent.getStringExtra("mail")

        // take the token to send it with bearer in header to change pass
        bearerToken += token

        Toast.makeText(this, "header: $bearerToken", Toast.LENGTH_SHORT).show()

        passEt = binding.passChangePassEt
        confirmEt = binding.passChangeConfirmPassEt
        confirmBtn = binding.changePassConfirmBtn

        confirmBtn.setOnClickListener(View.OnClickListener {
            confirmBtnClick()
        })

    }

    /**
     * A method called to handle sign up button clicks
     */
    private fun confirmBtnClick() {
        //check for all inputs from user are correct
        if (!util.isValidPassword(passEt.text.toString())) {
            passEt.setText("")
            passEt.hint = getString(R.string.pass_warning)
            passEt.setHintTextColor(resources.getColor(R.color.colorAccent))
            return
        } else if (confirmEt.text.toString().isNullOrEmpty()
                || !confirmEt.text.toString().equals(passEt.text.toString())) {
            confirmEt.setText("")
            confirmEt.hint = getString(R.string.fix_confirmPassWarning)
            passEt.setHintTextColor(resources.getColor(R.color.colorAccent))
            return
        }

        //create MobileUser object and set it's attributes
        val user = User(mail)
        user.password = passEt.text.toString()
        // this constructor iz wrong as it's for mail and otp verify
        // val user = User(mail, passEt.text.toString())


        Log.e("ChangePassActivity: ", user.toString())

        //sign up method that will call the web service
        changePassCall(user)


    }

    private fun changePassCall(user: User) {

        //create UserService object
        val userService = retrofit.create(UserService::class.java)

        //initialize the changePass call
        val call = userService.changePass(user, bearerToken)

        //initialize and show a progress dialog to the user
        val progressDialog = util.initProgress(this, getString(R.string.loading))
        progressDialog.show()

        //start the call
        call.enqueue(object : retrofit2.Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //pass changed successfully
                    progressDialog.dismiss()
//                    val intent = Intent(this@ChangePassActivity, ChangePassActivity::class.java)
//                    intent.putExtra("token", response.body()!!.token)
//                    intent.putExtra("mail", mail)
//                    startActivity(intent)
                    Toast.makeText(this@ChangePassActivity, "pass changed", Toast.LENGTH_SHORT).show()

//                    finish()
                } else if (responseCode == 434) {
                    //pass not saved successfully
                    progressDialog.dismiss()
                    Toast.makeText(this@ChangePassActivity, getString(R.string.pass_war), Toast.LENGTH_LONG).show()
                } else if (responseCode == 438) {
                    //pass not saved successfully
                    progressDialog.dismiss()
                    Toast.makeText(this@ChangePassActivity, getString(R.string.user_not_found), Toast.LENGTH_LONG).show()
                } else if (responseCode == 440) {
                    //pass not saved successfully
                    progressDialog.dismiss()
                    Toast.makeText(this@ChangePassActivity, getString(R.string.email_does_not_match), Toast.LENGTH_LONG).show()
                } else {
                    //user not saved successfully
                    progressDialog.dismiss()
                    Toast.makeText(this@ChangePassActivity, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@ChangePassActivity, getString(R.string.error_message), Toast.LENGTH_LONG).show()

            }
        })

    }


}