package com.emailmarketingtool.entities

import com.emailmarketingtool.enums.Role
import com.emailmarketingtool.utils.DataBaseUtils
import jakarta.persistence.*

@Entity
@Table(name = "auth_role")
data class AuthRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val name: Role
) {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "auth_role_permission",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id", referencedColumnName = "id")]
    )
    var permissions: MutableSet<AuthPermission> = mutableSetOf()
}
