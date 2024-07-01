package com.emailmarketingtool.entities

import com.emailmarketingtool.enums.UserStatus
import jakarta.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var firstName: String,
    var lastName: String,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "auth_user_id")
    val authUser: AuthUser,
    @Enumerated(EnumType.STRING)
    var status: UserStatus = UserStatus.ACTIVE,

) {
    fun getUserRole() = this.authUser.role.name
}
