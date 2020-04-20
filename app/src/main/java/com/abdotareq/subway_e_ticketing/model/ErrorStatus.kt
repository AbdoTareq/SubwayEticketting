package com.abdotareq.subway_e_ticketing.model

import android.app.Application
import com.abdotareq.subway_e_ticketing.R

class ErrorStatus {
    object Codes {
        const val NoNetworkException = -1

        private const val SocketTimeoutServerOffline = -2

        private const val PasswordLessThan_8 = 434

        private const val EmailAlreadyExist = 435

        private const val WrongEmailOrPassword = 436

        private const val WrongPassword = 437

        private const val UserNotFound = 438

        private const val WrongOtpToken = 439

        private const val EmailsDoesNotMatch = 440

        private const val WrongTicketPrice = 441

        private const val InvalidTicket = 442

        private const val NoTicketsFound = 443

        private const val CardsNotFound = 445

        private const val CardAlreadyExist = 446

        private const val InvalidCardNumber = 447

        private const val InvalidDate = 448

        private const val InvalidCVV = 455

        fun getErrorMessage(errorCode: Int, applicationCon: Application): String {
            return when (errorCode) {
                NoNetworkException -> applicationCon.getString(R.string.check_network)
                SocketTimeoutServerOffline -> applicationCon.getString(R.string.server_offline_try)
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
                else -> "else on error message"
            }
        }

    }

}