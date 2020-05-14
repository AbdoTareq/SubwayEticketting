package com.abdotareq.subway_e_ticketing.data.repository

import com.abdotareq.subway_e_ticketing.data.model.*
import com.abdotareq.subway_e_ticketing.data.network.UserApiObj
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException

class UserRepository {
    //Make your retrofit setup here
    private var job = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    //This is the method that calls API using Retrofit
    fun authenticate(user: User, registerInterface: RegisterInterface) {
        coroutineScope.launch {
            try {
                val token = UserApiObj.retrofitService.authenticate(user)
                registerInterface.onSuccess(token!!.token)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                registerInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                registerInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                registerInterface.onFail(e.toString())
            }
        }
    }

    fun authenticateGoogle(tokenSent: String, registerInterface: RegisterInterface) {
        coroutineScope.launch {
            try {
                val receivedToken = UserApiObj.retrofitService.authenticateWithGoogle(tokenSent)
                registerInterface.onSuccess(receivedToken!!.token)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                registerInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                registerInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                registerInterface.onFail(e.toString())
            }
        }
    }

    fun registerWithGoogle(tokenSent: String, birthDate: String, gender: String, registerInterface: RegisterInterface) {
        coroutineScope.launch {
            try {
                val receivedToken = UserApiObj.retrofitService.registerWithGoogle(tokenSent, birthDate, gender)
                registerInterface.onSuccess(receivedToken!!.token)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                registerInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                registerInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                registerInterface.onFail(e.toString())
            }
        }
    }

    fun signUpCall(user: User, registerInterface: RegisterInterface) {
        //start the call
        coroutineScope.launch {
            try {
                val token = UserApiObj.retrofitService.saveUser(user)
                registerInterface.onSuccess(token!!.token)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                registerInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                registerInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                registerInterface.onFail(e.toString())
            }
        }
    }

    fun updateUser(bearerToken: String, user: User, userInterface: UserInterface) {
        //start the call
        coroutineScope.launch {
            try {
                UserApiObj.retrofitService.updateUser(user, bearerToken)
                userInterface.onSuccess()
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                userInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                userInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                userInterface.onFail(e.toString())
            }
        }

    }

    fun getUserData(bearerToken: String, getUserInterface: GetUserInterface) {
        //start the call
        coroutineScope.launch {
            try {
                val user = UserApiObj.retrofitService.getUser(bearerToken)
                getUserInterface.onSuccess(user)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                getUserInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                getUserInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                getUserInterface.onFail(e.toString())
            }
        }
    }

    fun sendVerificationCode(user: User, userInterface: UserInterface) {
        //start the call
        coroutineScope.launch {
            try {
                UserApiObj.retrofitService.sendVerificationCode(user)
                userInterface.onSuccess()
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                userInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                userInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                try {
                    userInterface.onFail(e.toString())
                } catch (e: Exception) {
                    Timber.e(e)
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    fun verifyCode(user: User, registerInterface: RegisterInterface) {
        coroutineScope.launch {
            try {
                val token = UserApiObj.retrofitService.verifyCode(user)
                registerInterface.onSuccess(token!!.token)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                registerInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                registerInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                registerInterface.onFail(e.toString())
            }
        }
    }

    fun changePass(user: User, bearerToken: String, userInterface: UserInterface) {
        //start the call
        coroutineScope.launch {
            try {
                UserApiObj.retrofitService.changePass(user, bearerToken)
                userInterface.onSuccess()
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                userInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                userInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                userInterface.onFail(e.toString())
            }
        }
    }

    fun updatePass(userPassword: UserPassword, bearerToken: String, userInterface: UserInterface) {
        //start the call
        coroutineScope.launch {
            try {
                UserApiObj.retrofitService.updatePass(userPassword, bearerToken)
                userInterface.onSuccess()
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                userInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                userInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                FirebaseCrashlytics.getInstance().recordException(e)
                userInterface.onFail(e.toString())
            }
        }
    }

    fun cancelJob() {
        job.cancel()
    }

}