package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.AuthPermission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthPermissionRepository : JpaRepository<AuthPermission, Long> {
    fun findByNameIn(permissions: Set<String>): Set<AuthPermission>
}