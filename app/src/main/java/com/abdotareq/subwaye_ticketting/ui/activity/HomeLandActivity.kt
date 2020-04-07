package com.abdotareq.subwaye_ticketting.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.abdotareq.subwaye_ticketting.R
import kotlinx.android.synthetic.main.activity_home_land.*

class HomeLandActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_land)

        // this for icon background tint to be empty(just lines make image)
        bottom_navigation_main.itemIconTintList = null


    }

}
