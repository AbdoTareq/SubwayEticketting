package com.abdotareq.subway_e_ticketing.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.databinding.ActivityRegisterBinding
import com.rezwan.knetworklib.KNetwork

class RegisterActivity : AppCompatActivity(), KNetwork.OnNetWorkConnectivityListener {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // this for view binding to replace findviewbyid
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        KNetwork.bind(this, lifecycle)
                .setConnectivityListener(this)


    }

    override fun onNetConnected() {
        Toast.makeText(this, "connected", Toast.LENGTH_LONG).show()
    }

    override fun onNetDisConnected() {
        Toast.makeText(this, "disconnected", Toast.LENGTH_LONG).show()
    }


}