package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.User
import com.emailmarketingtool.enums.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.authUser.username = ?1")
    fun findUserByAuthUser(authUserName: String): User?
}