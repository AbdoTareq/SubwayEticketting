package com.abdotareq.subway_e_ticketing.viewmodels.register

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.repository.UserRepository

/**
 * ViewModel containing all the logic needed to sign in
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class GoogleRegisterViewModel(val tokenSent: String, application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository()
    private val applicationCon = application

    val gender = MutableLiveData<String>()
    val birthDate = MutableLiveData<String>()
    val buttonVisible = MutableLiveData<Int>()

    init {
        gender.value = applicationCon.getString(R.string.select_gender)
        birthDate.value = applicationCon.getString(R.string.select_birthday)
        buttonVisible.value = View.GONE
    }

    fun validate(): Boolean {
        if (gender.value != applicationCon.getString(R.string.select_gender) &&
                birthDate.value != applicationCon.getString(R.string.select_birthday)) {
            return true
        }
        return false
    }

    fun registerWithGoogle(registerInterface: RegisterInterface) {
        userRepo.registerWithGoogle(tokenSent, birthDate.value!!, gender.value!!, registerInterface)
    }

    fun getErrorMess(code: String): String {
        return getErrorMessage(code, applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        userRepo.cancelJob()
    }

}