package com.abdotareq.subway_e_ticketing.viewmodels.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.data.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.data.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.data.model.User
import com.abdotareq.subway_e_ticketing.data.repository.UserRepository
import com.abdotareq.subway_e_ticketing.utility.Util
import timber.log.Timber

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
    private val userRepo = UserRepository()
    private val applicationCon = application

    val first = MutableLiveData<String>()

    val last = MutableLiveData<String>()

    val mail = MutableLiveData<String>()

    val pass = MutableLiveData<String>()

    val confPass = MutableLiveData<String>()

    val gender = MutableLiveData<String>()

    val birthDate = MutableLiveData<String>()

    private val _eventRegister = MutableLiveData<Boolean>()
    val eventRegister: LiveData<Boolean>
        get() = _eventRegister

    private val _eventSignIn = MutableLiveData<Boolean>()
    val eventSignIn: LiveData<Boolean>
        get() = _eventSignIn

    init {
        gender.value = applicationCon.getString(R.string.select_gender)
        birthDate.value = applicationCon.getString(R.string.birth_date)
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
        if (mail.value.isNullOrEmpty() || Util.isValidEmail(mail.value)) {
            return false
        }
        return true
    }

    fun validatePass(pass: String): Boolean {
        if (pass.isEmpty() || !Util.isValidPassword(pass)) {
            return false
        }
        return true
    }

    /**
     * A method used to save a new user (sign up)
     */
    fun saveUserCall(registerInterface: RegisterInterface) {
        //create MobileUser object and set it's attributes
        val user = User(first_name = first.value,
                last_name = last.value,
                email = mail.value,
                password = pass.value,
                gender = gender.value,
                birth_date = birthDate.value,
                admin = 0
        )
        Timber.e(user.toString())

        //start the call
        userRepo.signUpCall(user, registerInterface)
    }

    fun getErrorMess(code: String): String {
        return getErrorMessage(code, applicationCon)
    }


    override fun onCleared() {
        super.onCleared()
        userRepo.cancelJob()
    }
}