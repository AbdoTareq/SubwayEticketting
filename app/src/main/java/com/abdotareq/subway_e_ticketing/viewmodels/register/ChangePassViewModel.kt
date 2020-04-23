package com.abdotareq.subway_e_ticketing.viewmodels.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.model.UserInterface
import com.abdotareq.subway_e_ticketing.repository.UserRepository
import timber.log.Timber

/**
 * ViewModel containing all the logic needed to sign in
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class ChangePassViewModel(mailProperty: String, tokenProperty: String, application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository()
    private val applicationCon = application

    private val _mail = MutableLiveData<String>()
    val mail: LiveData<String>
        get() = _mail

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    val pass = MutableLiveData<String>()

    val confirmPass = MutableLiveData<String>()

    init {
        _mail.value = mailProperty
        _token.value = tokenProperty
    }

    private val _eventChangePass = MutableLiveData<Boolean>()
    val eventChangePass: LiveData<Boolean>
        get() = _eventChangePass

    fun onChangePassComplete() {
        _eventChangePass.value = false
    }

    fun onChangePass() {
        _eventChangePass.value = true
    }

//    fun validateCode(): Boolean {
//        if (code.value.isNullOrEmpty()) {
//            return false
//        }
//        return true
//    }

    fun changePass(userInterface: UserInterface) {
        val bearerToken = "Bearer " + _token.value
        val user = User(email = _mail.value, password = pass.value)
        Timber.e(user.toString())

        // start the coroutine
        userRepo.changePass(user, bearerToken, userInterface)
    }

    fun getErrorMess(code: Int): String {
        return getErrorMessage(code, applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        userRepo.cancelJob()
    }

}