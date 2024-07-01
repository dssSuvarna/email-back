package com.emailmarketingtool.models

import com.emailmarketingtool.entities.AuthUser
import com.emailmarketingtool.enums.AuthUserStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val authUser: AuthUser) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(authUser.role.name.name))
    }

    override fun getPassword(): String = authUser.password

    override fun getUsername(): String = authUser.username

    override fun isAccountNonExpired(): Boolean = authUser.status != AuthUserStatus.DISABLED

    override fun isAccountNonLocked(): Boolean = authUser.status != AuthUserStatus.BLOCKED

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = authUser.status == AuthUserStatus.ENABLED
}