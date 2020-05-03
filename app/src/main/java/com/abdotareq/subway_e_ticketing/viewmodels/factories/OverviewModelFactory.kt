package com.abdotareq.subway_e_ticketing.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.viewmodels.home.OverviewViewModel

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the SleepDatabaseDao and context to the ViewModel.
 */
class OverviewModelFactory(private val bearerToken: String,
                           private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel(bearerToken, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

