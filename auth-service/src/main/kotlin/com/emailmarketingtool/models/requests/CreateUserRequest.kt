package com.emailmarketingtool.models.requests

import com.emailmarketingtool.enums.Role
import jakarta.validation.constraints.Pattern

data class CreateUserRequest(
    @field:Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$")
    val username: String,
    @field:Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    val password: String,
    var firstName: String,
    var lastName: String,
    val role: Role = Role.USER,
)