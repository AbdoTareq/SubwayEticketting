package com.abdotareq.subway_e_ticketing.controller.activity

import android.content.Intent
import android.widget.Toast
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.retrofit.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

class SplashScreenActivity : AwesomeSplash() {

    private var user: User? = null
    private var flag: Boolean = false

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!
    override fun initSplash(configSplash: ConfigSplash) {

        /* you don't have to override every property */

        // if user logged get his data
        if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this)) {
            getUserData(SharedPreferenceUtil.getSharedPrefsTokenId(this), configSplash)
        }else{
            //Customize Title
            configSplash.titleSplash = getString(R.string.app_name)
            configSplash.titleTextColor = R.color.colorWhite
            configSplash.titleTextSize = 30f //float value
            configSplash.animTitleDuration = 2000
            configSplash.animTitleTechnique = Techniques.FadeIn
            //        configSplash.setTitleFont("fonts/segoe_ui.ttf"); //provide string to your font located in assets/fonts/
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


    }


    private fun getUserData(userIdToken: String, configSplash: ConfigSplash) {
        var bearerToken: String = "Bearer "
        bearerToken += userIdToken

        //start the call
        UserApiObj.retrofitService.getUser(bearerToken).enqueue(object : retrofit2.Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {
                    //get user successfully
                    user = response.body()!!

                    configSplash.titleSplash = "${getString(R.string.welcome)} ${user?.first_name} ${user?.last_name} "
                    configSplash.animTitleDuration = 2000
                    configSplash.titleTextSize = 30f //float value
                    configSplash.animTitleTechnique = Techniques.FadeIn

                } else if (responseCode == 438) {
                    //pass not saved successfully
                    Toast.makeText(this@SplashScreenActivity, getString(R.string.pass_less), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.user_not_found))
                } else {
                    //user not saved successfully
                    Toast.makeText(this@SplashScreenActivity, getString(R.string.else_on_repsonse), Toast.LENGTH_LONG).show()
                    Timber.e(getString(R.string.else_on_repsonse))
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(this@SplashScreenActivity, getString(R.string.failure_happened), Toast.LENGTH_LONG).show()
                Timber.e("$t")
            }
        })
    }


    override fun animationsFinished() {
        //transit to another activity here
        //or do whatever you want
        val intent: Intent = if (SharedPreferenceUtil.getSharedPrefsLoggedIn(this@SplashScreenActivity)) {
            Intent(this@SplashScreenActivity, HomeLandActivity::class.java)

        } else
            Intent(this@SplashScreenActivity, RegisterActivity::class.java)
        try {
            intent.putExtra("user", user); // sending user object.
        } catch (e: Exception) {
            Timber.e("$e")
        }
        startActivity(intent)
        finish()

    }

}