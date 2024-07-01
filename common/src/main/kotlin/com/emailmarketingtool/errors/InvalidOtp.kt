package com.emailmarketingtool.errors

import org.springframework.http.HttpStatus

class InvalidOtp(
    override val statusCode: HttpStatus = HttpStatus.GATEWAY_TIMEOUT,
    override val message: String = ErrorMessages.INVALID_OTP
) : BaseError(statusCode, message)