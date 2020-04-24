package com.abdotareq.subway_e_ticketing.viewmodels.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.model.UserInterface
import com.abdotareq.subway_e_ticketing.repository.UserRepository
import com.abdotareq.subway_e_ticketing.utility.util
import timber.log.Timber

/**
 * ViewModel containing all the logic needed to sign in
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class ForgetPassViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository()
    private val applicationCon = application

    val mail = MutableLiveData<String>()

    private val _eventSendCode = MutableLiveData<Boolean>()
    val eventSendCode: LiveData<Boolean>
        get() = _eventSendCode

    fun onSendCodeComplete() {
        _eventSendCode.value = false
    }

    fun onSendCode() {
        _eventSendCode.value = true
    }

    fun validateMail(): Boolean {
        if (mail.value.isNullOrEmpty() || util.isValidEmail(mail.value)) {
            return false
        }
        return true
    }

    fun forgetPass(userInterface: UserInterface) {
        val user = User(email = mail.value)
        Timber.e(user.toString())

        //start the coroutine
        userRepo.sendVerificationCode(user, userInterface)
    }

    fun getErrorMess(code: String): String {
        return getErrorMessage(code, applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        userRepo.cancelJob()
    }

}