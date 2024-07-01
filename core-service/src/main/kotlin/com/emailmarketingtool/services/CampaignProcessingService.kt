package com.emailmarketingtool.services

import com.emailmarketingtool.entities.Campaign
import com.emailmarketingtool.entities.Notification
import com.emailmarketingtool.entities.Sender
import com.emailmarketingtool.enums.CampaignStatus
import com.emailmarketingtool.enums.NotificationStatus
import com.emailmarketingtool.repositories.CampaignRepository
import com.emailmarketingtool.repositories.ContactRepository
import com.emailmarketingtool.repositories.NotificationRepository
import com.emailmarketingtool.repositories.TemplateRepository
import jakarta.mail.*
import org.bouncycastle.cms.RecipientId.password
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*


@Service
@Transactional
class CampaignProcessingService @Autowired constructor(
    private val emailService: EmailService,
    private val contactRepository: ContactRepository,
    private val templateRepository: TemplateRepository,
    private val templateService: TemplateService,
    private val notificationRepository: NotificationRepository,
    private val campaignRepository: CampaignRepository
) {
    fun processCampaign(campaign: Campaign) {
        campaign.status = CampaignStatus.PROCESSING
        val contacts = campaign.contacts.map { it.id }
        var scheduleHour = -1L
        contacts.forEach {
            notificationRepository.save(
                Notification(
                    campaignId = campaign.id,
                    scheduledTime = LocalDateTime.now().plusHours(++scheduleHour),
                    noOfRetries = 0,
                    status = NotificationStatus.CREATED,
                    contactId = it,
                    senderId = campaign.campaignSenders[0].id,
                    templateId = campaign.templates[0].id
                )
            )
        }
        processNotification(campaign)
    }

    @Scheduled(cron = "0 * * * * *")
    fun processNotification(campaign: Campaign) {
//        val currentTime = LocalDateTime.now()
//        val notifications =
//            notificationRepository.findByScheduledTimeLessThanAndStatus(currentTime, NotificationStatus.CREATED)
        val notifications = notificationRepository.findByCampaignIdAndStatus(campaign.id, NotificationStatus.CREATED)
        notifications.forEach { processEmail(it) }
    }


    fun processEmail(notification: Notification) {
        val template = templateRepository.findById(notification.templateId).get()
        val contact = contactRepository.findById(notification.contactId).get()
        val campaign = campaignRepository.findById(notification.campaignId).get()
        val emailRequest = EmailRequest(
            subject = campaign.name,
            contact = contact,
            body = template.content
        )
        emailService.sendEmail(campaign.id, emailRequest, campaign.campaignSenders[0])
        Thread.sleep(100)
    }

    fun getMailSender(sender: Sender): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = sender.host
        mailSender.port = sender.port
        mailSender.username = sender.mail
        mailSender.password = sender.passKey

        return mailSender
    }
}