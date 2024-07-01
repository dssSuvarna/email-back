package com.emailmarketingtool.entities

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
data class Sender(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var mail: String,
    var port: Int,
    var host: String,
    var passKey: String,
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    var signatures: List<String>,
    var userId: Long
)

