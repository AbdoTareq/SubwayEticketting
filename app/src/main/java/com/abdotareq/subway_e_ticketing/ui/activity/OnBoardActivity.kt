package com.abdotareq.subway_e_ticketing.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage

class OnBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val scr1 = PaperOnboardingPage(getString(R.string.create_new_account),
                getString(R.string.tut_acc_mess),
                Color.parseColor("#678FB4"), R.drawable.create_account_image, R.drawable.create_account_image)
        val scr2 = PaperOnboardingPage(getString(R.string.buy_tickets),
                getString(R.string.tut_buy_tickets),
                Color.parseColor("#266fc3"), R.drawable.buy_tickets_image, R.drawable.buy_tickets_image)
        val scr3 = PaperOnboardingPage(getString(R.string.scan_code),
                getString(R.string.tut_scan_code),
                Color.parseColor("#f99565"), R.drawable.scan_code_image, R.drawable.scan_code_image)

        val elements: ArrayList<PaperOnboardingPage> = ArrayList()

        elements.add(scr1)
        elements.add(scr2)
        elements.add(scr3)

        val onBoardingFragment = PaperOnboardingFragment.newInstance(elements)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, onBoardingFragment)
        fragmentTransaction.commit()

        onBoardingFragment.setOnRightOutListener {
            tutorialFinished()
        }
    }

    private fun tutorialFinished() {
        val intent =
                if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this))
                    Intent(this@OnBoardActivity, HomeLandActivity::class.java)
                else
                    Intent(this@OnBoardActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

}