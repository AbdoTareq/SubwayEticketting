package com.abdotareq.subway_e_ticketing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subway_e_ticketing.databinding.ActivityMainBinding
import com.abdotareq.subway_e_ticketing.controller.activity.registration.SignInActivity
import com.abdotareq.subway_e_ticketing.controller.activity.registration.SignUpActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var signUpBtn :Button
    private lateinit var signinBtn :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // this for view binding to replace findviewbyid
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.getRoot()
        setContentView(view)

        Timber.e("test timber")

        signUpBtn = binding.mainActivitySignupBtn
        signinBtn = binding.mainActivitySigninBtn

        signUpBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        signinBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        }

    }


}