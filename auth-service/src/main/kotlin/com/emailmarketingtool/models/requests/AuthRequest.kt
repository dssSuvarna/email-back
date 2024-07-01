package com.emailmarketingtool.models.requests

import jakarta.validation.constraints.Pattern

data class AuthRequest(
    @field:Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$")
    val username: String,
    @field:Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    val password: String
)

