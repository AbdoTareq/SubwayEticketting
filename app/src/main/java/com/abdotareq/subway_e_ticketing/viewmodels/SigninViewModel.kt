package com.abdotareq.subway_e_ticketing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.model.Token
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    val mail = MutableLiveData<String>()
    private val _getMail = MutableLiveData<String>()
    val getMail: LiveData<String>
        get() = _getMail


    val pass = MutableLiveData<String>()
    private val _getPass = MutableLiveData<String>()
    val getPass: LiveData<String>
        get() = _getPass

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
        if (mail.value.isNullOrEmpty() || util.isValidEmail(mail.value)) {
            return false
        }
        return true
    }

    fun validatePass(): Boolean {
        if (pass.value.isNullOrEmpty() || !util.isValidPassword(pass.value)) {
            return false
        }
        return true
    }

    fun authenticateCall(user: User, registerInterface: RegisterInterface) {
        //start the call
        UserApiObj.retrofitService.authenticate(user)?.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {

                    Timber.e("token:    ${response.body()!!.token}")

                    registerInterface.onSuccess(response.body()!!.token)

                } else if (responseCode == 436) {
                    //user not authenticated successfully
                    registerInterface.onFail(responseCode)
                } else {
                    //user not authenticated successfully
                    registerInterface.onFail(responseCode)
                }
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                Timber.e("getText(R.string.error_message)${t.message}")
                registerInterface.onFail(-1)
            }
        })
    }
}