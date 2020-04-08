package com.abdotareq.subwaye_ticketting.utility

import android.app.Application
import com.abdotareq.subwaye_ticketting.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Controller responsible to start timber debug tree
 * */

class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()

//        this condition to prevent logs in release
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}