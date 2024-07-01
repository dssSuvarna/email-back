package com.emailmarketingtool.models

import com.emailmarketingtool.enums.Role
import com.fasterxml.jackson.annotation.JsonProperty

data class UserPrincipal(
    val username: String,
    val rolePermissions: RolePermission
)

data class RolePermission(
    @JsonProperty("role")
    val role: Role,
    @JsonProperty("permissions")
    val permissions: List<String>
)
