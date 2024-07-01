package com.emailmarketingtool.entities

import com.emailmarketingtool.enums.CampaignStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

@Entity
data class Campaign(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,
    var description: String,
    @Enumerated(EnumType.STRING)
    var status: CampaignStatus,
    var scheduleTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    var templates: List<Template>,
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    var contacts: List<Contact>,
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    var campaignSenders: List<Sender>,
    val createdBy: Long
)

