package com.abdotareq.subway_e_ticketing

import android.app.Application
import timber.log.Timber


/**
 * Controller responsible to start timber debug tree
 * */

class ApplicationController : Application() {
    companion object {
        lateinit var instance: ApplicationController
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        configureCrashReporting()

    }

    // this method to configure debug behavior to enable timber and disable firebase in debug mode
    // and vice versa in release mode
    private fun configureCrashReporting() {

        // this condition to prevent logs in release
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}