package com.emailmarketingtool.models.responses

import com.emailmarketingtool.enums.UserStatus

data class UsersResponse (
    val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val status: UserStatus
)