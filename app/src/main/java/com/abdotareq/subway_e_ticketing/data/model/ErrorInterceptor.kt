package com.abdotareq.subway_e_ticketing.data.model

import com.abdotareq.subway_e_ticketing.ApplicationController
import com.abdotareq.subway_e_ticketing.R
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.net.SocketTimeoutException

class ErrorInterceptor : Interceptor {
    object Codes {
        // network errors
        private const val SocketTimeoutServerOffline = -2
        private const val BadRequest = 400
        private const val UnauthorizedError = 401
        private const val Forbidden = 403
        private const val NotFound = 404

        // our custom system errors
        private const val PasswordLessThan_8 = 434

        private const val EmailAlreadyExist = 435

        private const val WrongEmailOrPassword = 436

        private const val WrongPassword = 437

        const val UserNotFound = 438

        private const val WrongOtpToken = 439

        private const val EmailsDoesNotMatch = 440

        private const val WrongTicketPrice = 441

        private const val InvalidTicket = 442

        const val NoTicketsFound = 443

        private const val CardsNotFound = 445

        private const val CardAlreadyExist = 446

        private const val InvalidCardNumber = 447

        private const val InvalidDate = 448

        private const val InvalidCVV = 455

        private const val InvalidToken = 459

        fun getErrorMessage(errorCode: Int): String {
            val applicationCon = ApplicationController.instance
            return when (errorCode) {
                BadRequest -> applicationCon.getString(R.string.bad_request)
                SocketTimeoutServerOffline -> applicationCon.getString(R.string.server_offline_try)
                UnauthorizedError -> applicationCon.getString(R.string.unauthorizedError)
                NotFound -> applicationCon.getString(R.string.notFound)
                Forbidden -> applicationCon.getString(R.string.forbidden_mess)

                PasswordLessThan_8 -> applicationCon.getString(R.string.pass_less)
                EmailAlreadyExist -> applicationCon.getString(R.string.mail_exist)

                WrongEmailOrPassword -> applicationCon.getString(R.string.wrong_mail_or_pass)
                WrongPassword -> applicationCon.getString(R.string.wrong_pass)
                UserNotFound -> applicationCon.getString(R.string.user_not_found)
                WrongOtpToken -> applicationCon.getString(R.string.wrong_code)

                EmailsDoesNotMatch -> applicationCon.getString(R.string.email_does_not_match)
                WrongTicketPrice -> applicationCon.getString(R.string.wrong_ticket_price)
                InvalidTicket -> applicationCon.getString(R.string.invalid_ticket)
                NoTicketsFound -> applicationCon.getString(R.string.no_tickets_found)

                CardsNotFound -> applicationCon.getString(R.string.cards_not_found)
                CardAlreadyExist -> applicationCon.getString(R.string.card_alread_exist)
                InvalidCardNumber -> applicationCon.getString(R.string.invalid_card_number)

                InvalidDate -> applicationCon.getString(R.string.invalid_date)
                InvalidCVV -> applicationCon.getString(R.string.invalid_cvv)
                InvalidToken -> applicationCon.getString(R.string.invalid_token)

                else -> errorCode.toString()
            }
        }

    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request: Request = chain.request()

        val response: Response
        response = try {
            chain.proceed(request)
        } catch (e: SocketTimeoutException) {
            Timber.e(Codes.getErrorMessage(-2))
            chain.proceed(request)
        }

        if (!response.isSuccessful)
            Timber.e(Codes.getErrorMessage(response.code))

        return response
    }
}