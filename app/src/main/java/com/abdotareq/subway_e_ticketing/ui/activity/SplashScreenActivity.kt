package com.abdotareq.subway_e_ticketing.ui.activity

import android.content.Intent
import android.widget.Toast
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.ui.activity.registration.SignInActivity
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

class SplashScreenActivity : AwesomeSplash() {
    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!
    override fun initSplash(configSplash: ConfigSplash) {

        /* you don't have to override every property */

        // if user logged get his data
        if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this)) {
            var user = getUserData(SharedPreferenceUtil.getSharedPrefsUserId(this))

            Timber.e("$user")
        }

        //Customize Circular Reveal
        configSplash.backgroundColor = R.color.colorPrimary //any color you want form colors.xml
        configSplash.animCircularRevealDuration = 2000 //int ms
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

    private fun getUserData(userIdToken: String): User {
        var bearerToken: String = "Bearer "
        bearerToken += userIdToken
        Timber.e("bearerToken:  $bearerToken ")
        var user = User()

        //start the call
        UserApiObj.retrofitService.getUser(bearerToken).enqueue(object : retrofit2.Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //get user successfully
                    Toast.makeText(this@SplashScreenActivity, "fname + ${response.body()!!.first_name} ", Toast.LENGTH_LONG).show()
                    user = response.body()!!
                } else if (responseCode == 438) {
                    //pass not saved successfully
                    Toast.makeText(this@SplashScreenActivity, getString(R.string.pass_war), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.user_not_found))
                } else {
                    //user not saved successfully
                    Toast.makeText(this@SplashScreenActivity, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.else_on_repsonse))
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(this@SplashScreenActivity, getString(R.string.failure_happened), Toast.LENGTH_LONG).show()
                Timber.e(getString(R.string.failure_happened))
            }
        })
        return user
    }

    override fun animationsFinished() {
        //transit to another activity here
        //or do whatever you want
        val intent: Intent = if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this))
            Intent(this, HomeLandActivity::class.java)
        else
            Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()

    }

}