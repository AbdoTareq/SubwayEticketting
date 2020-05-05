package com.abdotareq.subway_e_ticketing

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.rezwan.knetworklib.KNetwork
import io.fabric.sdk.android.Fabric
import timber.log.Timber


/**
 * Controller responsible to start timber debug tree
 * */

class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()

        KNetwork.initialize(this)

        configureCrashReporting()

    }

    // this method to configure debug behavior to enable timber and disable firebase in debug mode
    // and vice versa in release mode
    private fun configureCrashReporting() {
        val crashlyticsCore = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()
        Fabric.with(this, Crashlytics.Builder().core(crashlyticsCore).build())

        // this condition to prevent logs in release
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}