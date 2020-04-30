package com.abdotareq.subway_e_ticketing.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash

class SplashScreenActivity : AwesomeSplash() {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!
    override fun initSplash(configSplash: ConfigSplash) {

        // this for settings dark mode
        if (SharedPreferenceUtil.getSharedPrefsNightMode(this))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        /* you don't have to override every property */

        // if user logged get his data
        if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this)
                && !SharedPreferenceUtil.getSharedPrefsName(this).isNullOrEmpty()) {
            configSplash.titleSplash =
                    "${getString(R.string.welcome)} ${SharedPreferenceUtil.getSharedPrefsName(this)} "
        } else {
            //Customize Title
            configSplash.titleSplash = getString(R.string.app_name)
        }

        //Customize Circular Reveal
        configSplash.backgroundColor = R.color.primaryColor //any color you want form colors.xml
        configSplash.animCircularRevealDuration = 2000 //int ms
        configSplash.revealFlagX = Flags.REVEAL_RIGHT //or Flags.REVEAL_LEFT
        configSplash.revealFlagY = Flags.REVEAL_BOTTOM //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.logoSplash = R.drawable.white_logo //or any other drawable
        configSplash.animLogoSplashDuration = 1000 //int ms
        configSplash.animLogoSplashTechnique = Techniques.Landing //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Path
        /*   configSplash.setPathSplash(R.string.account_Settings.toString()); //set path String
           configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
           configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
           configSplash.setAnimPathStrokeDrawingDuration(3000);
           configSplash.setPathSplashStrokeSize(3); //I advise value be <5
           configSplash.setPathSplashStrokeColor(R.color.primaryTextColor); //any color you want form colors.xml
           configSplash.setAnimPathFillingDuration(3000);
           configSplash.setPathSplashFillColor(R.color.design_default_color_on_primary); //path object filling color*/

        configSplash.titleTextColor = R.color.secondaryTextColor
        configSplash.titleTextSize = 30f //float value
        configSplash.animTitleDuration = 2000
        configSplash.animTitleTechnique = Techniques.FadeIn

    }

    override fun animationsFinished() {
        //transit to another activity here
        //or do whatever you want
        val intent: Intent = if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this@SplashScreenActivity)) {
            Intent(this@SplashScreenActivity, HomeLandActivity::class.java)
        } else
            Intent(this@SplashScreenActivity, OnBoardActivity::class.java)

        startActivity(intent)
        finish()

    }

}