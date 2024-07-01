package com.emailmarketingtool.errors

import org.springframework.http.HttpStatus

open class BaseError(open val statusCode: HttpStatus, override val message:String): Error()