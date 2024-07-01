package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.AuthRole
import com.emailmarketingtool.enums.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<AuthRole, Long>{
}