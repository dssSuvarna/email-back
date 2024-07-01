package com.emailmarketingtool.models.responses

import com.emailmarketingtool.enums.CampaignStatus
import java.time.LocalDateTime

data class CampaignsResponse(
    val id: Long,
    val name: String,
    val description: String,
    val schedule_time: LocalDateTime,
    val end_time: LocalDateTime,
    val status: CampaignStatus,
    val template: TemplateResponse? = null,
    val contacts: List<ContactResponse>,
    val sender: SenderResponse

)