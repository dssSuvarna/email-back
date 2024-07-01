package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.Notification
import com.emailmarketingtool.enums.NotificationStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface NotificationRepository : JpaRepository<Notification, Long>{
    fun findByCampaignIdAndScheduledTimeLessThanAndStatus(
        campaignId: Long,
        date: LocalDateTime,
        status: NotificationStatus
    ): List<Notification>

    fun findByScheduledTimeLessThanAndStatus(scheduledTime: LocalDateTime, status: NotificationStatus): List<Notification>

    fun findByCampaignIdAndStatus(campaignId: Long, status: NotificationStatus): List<Notification>

    fun findByCampaignId(campaignId: Long): List<Notification>

    fun findAllByContactId(contactId: Long): List<Notification>

    fun findAllBySenderId(senderId: Long): List<Notification>

    fun findAllByTemplateId(templateId: Long): List<Notification>
}