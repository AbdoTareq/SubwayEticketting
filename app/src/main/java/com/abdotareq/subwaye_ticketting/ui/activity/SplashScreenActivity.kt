package com.abdotareq.subwaye_ticketting.ui.activity

import android.content.Intent
import com.abdotareq.subwaye_ticketting.R
import com.abdotareq.subwaye_ticketting.ui.activity.regesteriation.SignInActivity
import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash

class SplashScreenActivity : AwesomeSplash() {
    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!
    override fun initSplash(configSplash: ConfigSplash) {

        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.backgroundColor = R.color.colorPrimary //any color you want form colors.xml
        configSplash.animCircularRevealDuration = 1000 //int ms
        configSplash.revealFlagX = Flags.REVEAL_RIGHT //or Flags.REVEAL_LEFT
        configSplash.revealFlagY = Flags.REVEAL_BOTTOM //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.logoSplash = R.drawable.white_logo //or any other drawable
        configSplash.animLogoSplashDuration = 2000 //int ms
        configSplash.animLogoSplashTechnique = Techniques.Landing //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
//        configSplash.setPathSplash(String.valueOf(R.drawable.white_logo)); //set path String
//        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
//        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
//        configSplash.setAnimPathStrokeDrawingDuration(3000);
//        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
//        configSplash.setPathSplashStrokeColor(R.color.colorAccent); //any color you want form colors.xml
//        configSplash.setAnimPathFillingDuration(3000);
//        configSplash.setPathSplashFillColor(R.color.design_default_color_on_primary); //path object filling color


        //Customize Title
        configSplash.titleSplash = getString(R.string.app_name)
        configSplash.titleTextColor = R.color.colorWhite
        configSplash.titleTextSize = 30f //float value
        configSplash.animTitleDuration = 2000
        configSplash.animTitleTechnique = Techniques.FadeIn
        //        configSplash.setTitleFont("fonts/segoe_ui.ttf"); //provide string to your font located in assets/fonts/
    }

    override fun animationsFinished() {
        //transit to another activity here
        //or do whatever you want
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish();
    }
}