package com.abdotareq.subway_e_ticketing.viewmodels.factories


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdotareq.subway_e_ticketing.viewmodels.home.*
import com.abdotareq.subway_e_ticketing.viewmodels.register.*

/**
 *  This the class responsible for creating viewModels
 *  We cannot directly create the object of the ViewModel as it would not be aware of the lifeCycleOwner
 *
 *  [arg1] is mail or token(depends on the caller class)
 *  [arg2] is mail
 * */

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val application: Application,
                       private val arg1: String = "",
                       private val arg2: String = "") : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            // [arg1] is token
            modelClass.isAssignableFrom(GoogleRegisterViewModel::class.java) -> {
                GoogleRegisterViewModel(arg1, application) as T
            }
            modelClass.isAssignableFrom(TicketsTypeViewModel::class.java) -> {
                TicketsTypeViewModel(arg1, application) as T
            }
            modelClass.isAssignableFrom(InUseViewModel::class.java) -> {
                InUseViewModel(arg1, application) as T
            }
            modelClass.isAssignableFrom(AvailableViewModel::class.java) -> {
                AvailableViewModel(arg1, application) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(arg1, application) as T
            }
            modelClass.isAssignableFrom(OverviewViewModel::class.java) -> {
                OverviewViewModel(arg1, application) as T
            }
            // [arg1] is mail
            modelClass.isAssignableFrom(VerificationViewModel::class.java) -> {
                VerificationViewModel(arg1, application) as T
            }
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(application) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(application) as T
            }
            modelClass.isAssignableFrom(ForgetPassViewModel::class.java) -> {
                ForgetPassViewModel(application) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(application) as T
            }
            // arg1 mail , arg2 token
            modelClass.isAssignableFrom(ChangePassViewModel::class.java) -> {
                ChangePassViewModel(arg1, arg2, application) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
