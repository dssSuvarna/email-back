package com.emailmarketingtool.entities

import com.emailmarketingtool.enums.NotificationStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notification")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var scheduledTime: LocalDateTime,
    val campaignId: Long,
    var noOfRetries: Int,
    @Enumerated(EnumType.STRING)
    var status: NotificationStatus,
    var senderId: Long,
    var contactId: Long,
    var templateId: Long
)
