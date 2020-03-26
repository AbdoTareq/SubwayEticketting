package com.abdotareq.subwaye_ticketting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subwaye_ticketting.databinding.ActivityMainBinding
import com.abdotareq.subwaye_ticketting.ui.activity.regesteriation.SignInActivity
import com.abdotareq.subwaye_ticketting.ui.activity.regesteriation.SignUpActivity

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