package com.emailmarketingtool.configs

import com.emailmarketingtool.models.UserPrincipal
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
 class CustomPermissionEvaluator: PermissionEvaluator {
     override fun hasPermission(authentication: Authentication?, accessType: Any, permission: Any): Boolean {
         return if (authentication != null && accessType is String) {
             authentication.principal
             if ("hasAccess".equals(accessType.toString(), ignoreCase = true)) {
                 validateAccess(permission.toString(), authentication.principal as UserPrincipal)
             } else false
         } else false
     }

     private fun validateAccess(permission: String, userPrincipal: UserPrincipal): Boolean {
        return userPrincipal.rolePermissions.permissions.contains(permission)
     }

     override fun hasPermission(
         authentication: Authentication?, serializable: Serializable?, targetType: String?,
         permission: Any?
     ): Boolean {
         return false
     }
}