

package com.abdotareq.subway_e_ticketing.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.viewmodels.home.PocketViewModel

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the SleepDatabaseDao and context to the ViewModel.
 */
class PocketViewModelFactory(private val token: String, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PocketViewModel::class.java)) {
            return PocketViewModel(token, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

