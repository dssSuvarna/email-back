package com.emailmarketingtool.models.responses

import com.emailmarketingtool.models.RolePermission
import com.fasterxml.jackson.annotation.JsonProperty

data class UserRolesPermissionsResponse(
    @JsonProperty("userId")
    val userId: Long,
    @JsonProperty("rolePermissions")
    val rolePermissions: RolePermission
)