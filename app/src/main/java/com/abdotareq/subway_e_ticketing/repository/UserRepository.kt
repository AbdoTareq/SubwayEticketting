package com.abdotareq.subway_e_ticketing.repository

import android.widget.Toast
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.GetUserInterface
import com.abdotareq.subway_e_ticketing.model.UpdateUserInterface
import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import com.abdotareq.subway_e_ticketing.utility.util
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

    fun updateUser(bearerToken: String, user: User, updateUserInterface: UpdateUserInterface) {
        //start the call
        coroutineScope.launch {
            try {
                UserApiObj.retrofitService.updateUser(user, bearerToken)
                updateUserInterface.onSuccess()
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

    fun cancelJob() {
        job.cancel()
    }

}