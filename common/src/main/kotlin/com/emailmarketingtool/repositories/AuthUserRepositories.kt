package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.AuthUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthUserRepository: JpaRepository<AuthUser, Long> {
    fun findByUsername(username: String): AuthUser?
}