package com.emailmarketingtool.models.requests
import com.emailmarketingtool.entities.Contact
import com.emailmarketingtool.entities.Sender
import com.emailmarketingtool.entities.Template
import com.emailmarketingtool.enums.CampaignStatus
import java.time.LocalDateTime

data class UpdateCampaignRequest(
    val campaignId: Long,
    var name: String? = null,
    var description: String? = null,
    var status: CampaignStatus? = null,
    var scheduleTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val templates: List<Template>? = null,
    val senders: List<Sender>? = null,
    val contacts: List<Contact>? = null
)