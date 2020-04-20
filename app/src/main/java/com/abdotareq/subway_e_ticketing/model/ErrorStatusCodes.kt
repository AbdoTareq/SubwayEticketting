package com.abdotareq.subway_e_ticketing.model

import java.lang.Exception

object ErrorStatusCodes {

    const val SocketTimeoutServerOffline = -2
    const val Exception = -1

    const val PasswordLessThan_8 = 434
    const val EmailAlreadyExist = 435
    const val WrongEmailOrPassword = 436
    const val WrongPassword = 437
    const val UserNotFound = 438
    const val WrongOtpToken = 439
    const val EmailsDoesNotMatch = 440
    const val WrongTicketPrice = 441
    const val InvalidTicket = 442
    const val NoTicketsFound = 443
    const val CardsNotFound = 445
    const val CardAlreadyExist = 446
    const val InvalidCardNumber = 447
    const val InvalidDate = 448
    const val InvalidCVV = 455


}
