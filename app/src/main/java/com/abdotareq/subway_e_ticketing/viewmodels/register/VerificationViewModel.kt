package com.abdotareq.subway_e_ticketing.viewmodels.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.repository.UserRepository
import timber.log.Timber

/**
 * ViewModel containing all the logic needed to sign in
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class VerificationViewModel(mailProperty: String, application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository()
    private val applicationCon = application

    private val _mail = MutableLiveData<String>()
    val mail: LiveData<String>
        get() = _mail

    val code = MutableLiveData<String>()

    init {
        _mail.value = mailProperty
    }

    private val _eventContinue = MutableLiveData<Boolean>()
    val eventContinue: LiveData<Boolean>
        get() = _eventContinue

    fun onContinueComplete() {
        _eventContinue.value = false
    }

    fun onContinue() {
        _eventContinue.value = true
    }

    fun validateCode(): Boolean {
        if (code.value.isNullOrEmpty()) {
            return false
        }
        return true
    }

    fun verifyCode(registerInterface: RegisterInterface) {
        val user = User(email = _mail.value, otp_token = code.value)
        Timber.e(user.toString())

        // start the coroutine
        userRepo.verifyCode(user, registerInterface)
    }

    fun getErrorMess(code: String): String {
        return getErrorMessage(code, applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        userRepo.cancelJob()
    }

}