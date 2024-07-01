package com.emailmarketingtool.configs


import com.emailmarketingtool.entities.AuthPermission
import com.emailmarketingtool.entities.AuthRole
import com.emailmarketingtool.enums.RoleHolder
import com.emailmarketingtool.repositories.AuthPermissionRepository
import com.emailmarketingtool.repositories.AuthRoleRepository
import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Transactional
class RoleMigrationService @Autowired constructor(
    val authPermissionRepository: AuthPermissionRepository,
    val authRoleRepository: AuthRoleRepository
) {

    @PostConstruct
    fun migrateRoles() {
        // Permissions to delete
        val actualPermissions = RoleHolder.values().flatMap { it.permissions }.toSet()
        val dbPermissions = authPermissionRepository.findAll().map { it.name }.toSet()
        val permissionsToDelete = dbPermissions.subtract(actualPermissions)
        authPermissionRepository.findByNameIn(permissionsToDelete).forEach {
            authRoleRepository.findByPermissions(listOf(it.name)).forEach { role ->
                role.permissions.remove(it)
                authRoleRepository.save(role)
            }
        }
        authPermissionRepository.findByNameIn(permissionsToDelete).forEach {
            authPermissionRepository.delete(it)
        }

        // Roles to delete
        val actualRoles = RoleHolder.values().map { it.roleType }.toSet()
        val dbRoles = authRoleRepository.findAll().map { it.name }.toSet()
        val rolesToDelete = authRoleRepository.findByNameIn(dbRoles.subtract(actualRoles))
        authRoleRepository.deleteAll(rolesToDelete)

        // Permissions to add
        val permissionsToAdd = actualPermissions.minus(dbPermissions)
        authPermissionRepository.saveAll(permissionsToAdd.map { AuthPermission(name = it) })
        // Roles to add
        RoleHolder.values().forEach {
            val roleOpt = authRoleRepository.getAuthRoleByName(it.roleType)
            val authRole = roleOpt ?: authRoleRepository.save(AuthRole(name = it.roleType))
            authRole.permissions = authPermissionRepository.findByNameIn(it.permissions).toMutableSet()
            authRoleRepository.save(authRole)
        }
        authRoleRepository.findAll()
    }
}