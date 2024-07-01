package com.emailmarketingtool.entities

import com.emailmarketingtool.enums.AuthUserStatus
import com.emailmarketingtool.utils.DataBaseUtils
import jakarta.persistence.*

@Entity
@Table(name = "auth_user")
data class AuthUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var username: String,
    var password: String,
    @Enumerated(EnumType.STRING)
    var status: AuthUserStatus = AuthUserStatus.ENABLED,
    @OneToOne
    @JoinColumn(name = "auth_role_id")
    var role: AuthRole
)
