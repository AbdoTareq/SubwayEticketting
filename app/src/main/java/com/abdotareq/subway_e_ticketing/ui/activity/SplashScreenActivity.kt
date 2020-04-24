package com.abdotareq.subway_e_ticketing.ui.activity

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.GetUserInterface
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.repository.UserRepository
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash
import timber.log.Timber

class SplashScreenActivity : AwesomeSplash() {

    private lateinit var userRepository: UserRepository
    private lateinit var getUserInterface: GetUserInterface

    private var user: User? = null

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!
    override fun initSplash(configSplash: ConfigSplash) {

        userRepository = UserRepository()

        // this for settings dark mode
        if (SharedPreferenceUtil.getSharedPrefsNightMode(this))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        /* you don't have to override every property */

        // if user logged get his data
        if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this)) {
            getUserData(SharedPreferenceUtil.getSharedPrefsTokenId(this), configSplash)
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

    private fun getUserData(userIdToken: String, configSplash: ConfigSplash) {
        var bearerToken = "Bearer "
        bearerToken += userIdToken

        getUserInterface = object : GetUserInterface {
            override fun onSuccess(userPassed: User) {
                user = userPassed
                configSplash.titleSplash = "${getString(R.string.welcome)} ${user?.first_name} ${user?.last_name} "
            }

            override fun onFail(responseCode: Int) {
                Toast.makeText(this@SplashScreenActivity, getErrorMessage(responseCode, application), Toast.LENGTH_LONG).show()
                configSplash.titleSplash = getString(R.string.app_name)
            }
        }
        userRepository.getUserData(bearerToken, getUserInterface)
    }

    override fun animationsFinished() {
        //transit to another activity here
        //or do whatever you want
        val intent: Intent = if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this@SplashScreenActivity)) {
            Intent(this@SplashScreenActivity, HomeLandActivity::class.java)
        } else
            Intent(this@SplashScreenActivity, OnBoardActivity::class.java)
        try {
//            user?.birth_date = user!!.birth_date?.substring(0..9)
//            intent.putExtra("user", user) // sending user object.
        } catch (e: Exception) {
            Timber.e("$e")
        }
        startActivity(intent)
        finish()

    }

}