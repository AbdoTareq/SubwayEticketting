package com.abdotareq.subway_e_ticketing.viewmodels.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.data.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.data.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.data.model.User
import com.abdotareq.subway_e_ticketing.data.repository.UserRepository
import com.abdotareq.subway_e_ticketing.utility.Util
import timber.log.Timber

/**
 * ViewModel containing all the logic needed to sign in
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class SigninViewModel(application: Application) : AndroidViewModel(application) {

    /**
     *  Don't expose:   private val _pass = MutableLiveData<String>()
    val pass: LiveData<String>
    get() = _pass
    as this make errors for a reason and will not work I swear ( val pass: LiveData<String> get() = _pass) makes big error
     * */

    private val userRepo = UserRepository()
    private val applicationCon = application

    val mail = MutableLiveData<String>()

    val pass = MutableLiveData<String>()

    private val _eventAuthenticate = MutableLiveData<Boolean>()
    val eventAuthenticate: LiveData<Boolean>
        get() = _eventAuthenticate

    private val _eventSignUp = MutableLiveData<Boolean>()
    val eventSignUp: LiveData<Boolean>
        get() = _eventSignUp

    private val _eventRecoverPass = MutableLiveData<Boolean>()
    val eventRecoverPass: LiveData<Boolean>
        get() = _eventRecoverPass

    fun onAuthenticateComplete() {
        _eventAuthenticate.value = false
    }

    fun onAuthenticate() {
        _eventAuthenticate.value = true
    }

    fun onSignUpComplete() {
        _eventSignUp.value = false
    }

    fun onSignUp() {
        _eventSignUp.value = true
    }

    fun onRecoverPassComplete() {
        _eventRecoverPass.value = false
    }

    fun onRecoverPass() {
        _eventRecoverPass.value = true
    }

    fun validateMail(): Boolean {
        if (mail.value.isNullOrEmpty() || Util.isValidEmail(mail.value)) {
            return false
        }
        return true
    }

    fun validatePass(): Boolean {
        if (pass.value.isNullOrEmpty() || !Util.isValidPassword(pass.value)) {
            return false
        }
        return true
    }

    fun authenticateCall(registerInterface: RegisterInterface) {
        val user = User(email = mail.value, password = pass.value)
        Timber.e(user.toString())

        userRepo.authenticate(user, registerInterface)
    }

    fun authenticateGoogle(tokenSent: String, registerInterface: RegisterInterface) {
        userRepo.authenticateGoogle(tokenSent, registerInterface)
    }

    fun getErrorMess(code: String): String {
        return getErrorMessage(code, applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        userRepo.cancelJob()
    }

}