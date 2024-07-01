package com.emailmarketingtool.entities

import com.emailmarketingtool.utils.DataBaseUtils
import jakarta.persistence.*


@Entity
@Table(name = "auth_permission")
data class AuthPermission(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String
)
