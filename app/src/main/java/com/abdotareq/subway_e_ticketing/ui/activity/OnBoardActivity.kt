package com.abdotareq.subway_e_ticketing.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.crashlytics.android.Crashlytics
import com.hololo.tutorial.library.Step
import com.hololo.tutorial.library.TutorialActivity


class OnBoardActivity : TutorialActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // for firebase crash reports
//        val crashButton = Button(this)
//        crashButton.text = "Crash!"
//        crashButton.setOnClickListener {
//            Crashlytics.getInstance().crash() // Force a crash
//        }
//
//        addContentView(crashButton, ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT))

        addFragment(Step.Builder().setTitle(getString(R.string.create_new_account))
                .setContent(getString(R.string.tut_acc_mess))
                .setBackgroundColor(ContextCompat.getColor(this, R.color.darkGray)) // int background color
                .setDrawable(R.drawable.create_account_image) // int top drawable
                .build())

        addFragment(Step.Builder().setTitle(getString(R.string.buy_tickets))
                .setContent(getString(R.string.tut_buy_tickets))
                .setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlue)) // int background color
                .setDrawable(R.drawable.buy_tickets_image) // int top drawable
                .build())

        addFragment(Step.Builder().setTitle(getString(R.string.scan_code))
                .setContent(getString(R.string.tut_scan_code))
                .setBackgroundColor(ContextCompat.getColor(this, R.color.darkOrange)) // int background color
                .setDrawable(R.drawable.scan_code_image) // int top drawable
                .build())

    }

    override fun finishTutorial() {
        // Your implementation
        val intent: Intent = if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this@OnBoardActivity))
            Intent(this@OnBoardActivity, HomeLandActivity::class.java)
        else
            Intent(this@OnBoardActivity, RegisterActivity::class.java)

        startActivity(intent)
        finish()
    }

    override fun currentFragmentPosition(position: Int) {

    }
}