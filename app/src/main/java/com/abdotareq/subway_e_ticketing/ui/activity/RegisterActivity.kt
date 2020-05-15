package com.abdotareq.subway_e_ticketing.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subway_e_ticketing.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // this for view binding to replace findviewbyid
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }

}