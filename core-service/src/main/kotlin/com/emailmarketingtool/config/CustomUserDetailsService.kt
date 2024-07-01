package com.emailmarketingtool.config

import com.emailmarketingtool.models.CustomUserDetails
import com.emailmarketingtool.errors.ErrorMessages.Companion.AUTH_USER_NOT_FOUND
import com.emailmarketingtool.repositories.AuthUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService @Autowired constructor(val authUserRepository: AuthUserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val authUser = authUserRepository.findByUsername(username) ?: throw UsernameNotFoundException(AUTH_USER_NOT_FOUND)
        return CustomUserDetails(authUser)
    }
}
