package com.abdotareq.subway_e_ticketing.controller.fragment.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SigninViewModel : ViewModel() {

    private val _mail = MutableLiveData<String>()
    val mail: LiveData<String>
        get() = _mail

    private val _pass = MutableLiveData<String>()
    val pass: LiveData<String>
        get() = _pass

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

}