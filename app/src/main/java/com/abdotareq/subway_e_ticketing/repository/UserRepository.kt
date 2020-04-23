package com.abdotareq.subway_e_ticketing.repository

import com.abdotareq.subway_e_ticketing.model.GetUserInterface
import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.model.UserInterface
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.network.UserApiObj
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
                registerInterface.onFail(e.code())
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                registerInterface.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                registerInterface.onFail(-1)
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
                registerInterface.onFail(e.code())
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                registerInterface.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                registerInterface.onFail(-1)
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
                userInterface.onFail(e.code())
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                userInterface.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                userInterface.onFail(-1)
            }
        }

    }

    fun getUserData(bearerToken: String, updateUserInterface: GetUserInterface) {
        //start the call
        coroutineScope.launch {
            try {
                val user = UserApiObj.retrofitService.getUser(bearerToken)
                updateUserInterface.onSuccess(user)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                updateUserInterface.onFail(e.code())
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                updateUserInterface.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                updateUserInterface.onFail(-1)
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
                userInterface.onFail(e.code())
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                userInterface.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                userInterface.onFail(-1)
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
                registerInterface.onFail(e.code())
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                registerInterface.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                registerInterface.onFail(-1)
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
                userInterface.onFail(e.code())
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                userInterface.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                userInterface.onFail(-1)
            }
        }
    }


    fun cancelJob() {
        job.cancel()
    }

}