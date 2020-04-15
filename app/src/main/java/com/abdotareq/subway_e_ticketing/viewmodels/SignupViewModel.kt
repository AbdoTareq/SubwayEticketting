package com.abdotareq.subway_e_ticketing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.utility.util

/**
 * ViewModel containing all the logic needed to sign up
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during activity
 * or fragment lifecycle events.
 */
class SignupViewModel(application: Application) : AndroidViewModel(application) {

    /**
     *  Don't expose:   private val _pass = MutableLiveData<String>()
                        val pass: LiveData<String>
                        get() = _pass
    as this make errors for a reason and will not work I swear ( val pass: LiveData<String> get() = _pass) makes big error
     * */

    val first = MutableLiveData<String>()
    private val _getFirst = MutableLiveData<String>()
    val getFirst: LiveData<String>
        get() = _getFirst

    val last = MutableLiveData<String>()
    private val _getLast = MutableLiveData<String>()
    val getLast: LiveData<String>
        get() = _getLast

    val mail = MutableLiveData<String>()
    private val _getMail = MutableLiveData<String>()
    val getMail: LiveData<String>
        get() = _getMail

    val pass = MutableLiveData<String>()
    private val _getPass = MutableLiveData<String>()
    val getPass: LiveData<String>
        get() = _getPass

    val confPass = MutableLiveData<String>()
    private val _getConPass = MutableLiveData<String>()
    val getConPass: LiveData<String>
        get() = _getConPass

    val gender = MutableLiveData<String>()
    private val _getGender = MutableLiveData<String>()
    val getGender: LiveData<String>
        get() = _getGender

    val birthDate = MutableLiveData<String>()
    private val _getBirthDate = MutableLiveData<String>()
    val getBirthDate: LiveData<String>
        get() = _getBirthDate

    private val _eventRegister = MutableLiveData<Boolean>()
    val eventRegister: LiveData<Boolean>
        get() = _eventRegister

    private val _eventSignIn = MutableLiveData<Boolean>()
    val eventSignIn: LiveData<Boolean>
        get() = _eventSignIn

    init {
        gender.value = "Gender"
        birthDate.value = "Birth Date"
    }

    fun onRegisterComplete() {
        _eventRegister.value = false
    }

    fun onRegister() {
        _eventRegister.value = true
    }

    fun onSignInComplete() {
        _eventSignIn.value = false
    }

    fun onSignIn() {
        _eventSignIn.value = true
    }

    fun validateMail(): Boolean {
        if (mail.value.isNullOrEmpty() || util.isValidEmail(mail.value)) {
            return false
        }
        return true
    }

    fun validatePass(pass: String): Boolean {
        if (pass.isEmpty() || !util.isValidPassword(pass)) {
            return false
        }
        return true
    }




}