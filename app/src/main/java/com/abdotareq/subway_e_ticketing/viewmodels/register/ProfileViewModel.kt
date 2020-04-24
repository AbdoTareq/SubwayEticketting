package com.abdotareq.subway_e_ticketing.viewmodels.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.GetUserInterface
import com.abdotareq.subway_e_ticketing.model.UserInterface
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.repository.UserRepository
import timber.log.Timber


/**
 * ViewModel containing all the logic needed to sign up
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during activity
 * or fragment lifecycle events.
 */
class ProfileViewModel(userProperty: User, application: Application) : AndroidViewModel(application) {

    /**
     *  Don't expose:   private val _pass = MutableLiveData<String>()
    val pass: LiveData<String>
    get() = _pass
    as this make errors for a reason and will not work I swear ( val pass: LiveData<String> get() = _pass) makes big error
     * */
    private val userRepo = UserRepository()

    private val applicationCon = application

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _eventLogout = MutableLiveData<Boolean>()
    val eventLogout: LiveData<Boolean>
        get() = _eventLogout

    private val _eventChangePass = MutableLiveData<Boolean>()
    val eventChangePass: LiveData<Boolean>
        get() = _eventChangePass

    private val _eventChangePhoto = MutableLiveData<Boolean>()
    val eventChangePhoto: LiveData<Boolean>
        get() = _eventChangePhoto

    private val _eventSave = MutableLiveData<Boolean>()
    val eventSave: LiveData<Boolean>
        get() = _eventSave

    private val _eventBirth = MutableLiveData<Boolean>()
    val eventBirth: LiveData<Boolean>
        get() = _eventBirth

    private val _eventGender = MutableLiveData<Boolean>()
    val eventGender: LiveData<Boolean>
        get() = _eventGender

    init {
        _user.value = userProperty
        _user.value!!.birth_date?.substring(0..9)
    }

    fun onLogoutComplete() {
        _eventLogout.value = false
    }

    fun onLogout() {
        _eventLogout.value = true
    }

    fun onChangePassComplete() {
        _eventChangePass.value = false
    }

    fun onChangePass() {
        _eventChangePass.value = true
    }

    fun onChangePhotoComplete() {
        _eventChangePhoto.value = false
    }

    fun onChangePhoto() {
        _eventChangePhoto.value = true
    }

    fun onSaveComplete() {
        _eventSave.value = false
    }

    fun onSave() {
        _eventSave.value = true
    }

    fun onBirthComplete() {
        _eventBirth.value = false
    }

    fun onBirth() {
        _eventBirth.value = true
    }

    fun onGenderComplete() {
        _eventGender.value = false
    }

    fun onGender() {
        _eventGender.value = true
    }

    /**
     * A method used to save a new user (sign up)
     */
    fun saveUserCall(bearerToken: String, userImage: String?, userId: Int, userInterface: UserInterface) {
        //create MobileUser object and set it's attributes

        _user.value!!.id = userId
        _user.value!!.image = userImage
        Timber.e("${_user.value}")

        //start the call
        userRepo.updateUser(bearerToken, _user.value!!, userInterface)
    }

    fun getUser(bearerToken: String, getUserInterface: GetUserInterface) {
        //start the coroutine
        userRepo.getUserData(bearerToken, getUserInterface)
    }

    // The displayPropertyBirth formatted Transformation Map LiveData, which displays the sale
    // or rental price.
//    val displayPropertyBirth = Transformations.map(user) {
//        it.birth_date?.substring(0..9)
//    }

    fun getErrorMess(code: Int): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        userRepo.cancelJob()
    }
}