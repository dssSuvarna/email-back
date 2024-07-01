package com.emailmarketingtool.errors

import org.springframework.http.HttpStatus

class InvalidToken(
    override val statusCode: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val message: String = ErrorMessages.INVALID_TOKEN
) : BaseError(statusCode, message)