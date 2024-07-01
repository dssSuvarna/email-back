package com.emailmarketingtool.exceptions

import com.emailmarketingtool.errors.BaseError

open class BaseException(val error: BaseError) : RuntimeException()