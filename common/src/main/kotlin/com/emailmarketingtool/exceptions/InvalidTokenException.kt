package com.emailmarketingtool.exceptions

import com.emailmarketingtool.errors.BaseError

class InvalidTokenException(error: BaseError) : BaseException(error)