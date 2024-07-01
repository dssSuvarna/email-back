package com.emailmarketingtool.exceptions

import com.emailmarketingtool.errors.BaseError


class BadCredentialsException(error: BaseError) : BaseException(error)
