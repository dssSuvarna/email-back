package com.emailmarketingtool.errors

import org.springframework.http.HttpStatus

class MisMatchPassword(
    override val statusCode: HttpStatus = HttpStatus.BAD_REQUEST,
    override val message: String = ErrorMessages.MISMATCH_PASSWORD
) : BaseError(statusCode, message)