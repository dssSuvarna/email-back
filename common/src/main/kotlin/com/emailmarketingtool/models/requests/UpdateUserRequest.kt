package com.emailmarketingtool.models.requests



data class UpdateUserRequest (
    val id: Long,
    val username: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
)