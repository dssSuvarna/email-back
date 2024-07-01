package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.AuthRole
import com.emailmarketingtool.enums.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AuthRoleRepository : JpaRepository<AuthRole, Long> {
    fun getAuthRoleByName(name: Role): AuthRole?

    @Query("SELECT ar FROM AuthRole ar JOIN ar.permissions permission WHERE permission.name IN (?1)")
    fun findByPermissions(permissions: List<String>): List<AuthRole>

    fun findByNameIn(names:Set<Role>): List<AuthRole>
}