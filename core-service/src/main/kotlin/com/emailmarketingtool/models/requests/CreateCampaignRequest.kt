package com.emailmarketingtool.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CreateCampaignRequest(
    @JsonProperty("name")
    var name: String,
    @JsonProperty("description")
    var description: String,
    @JsonProperty("scheduleTime")
    var scheduleTime: LocalDateTime,
    @JsonProperty("endTime")
    val endTime: LocalDateTime,
    @JsonProperty("templateIds")
    val templateIds: List<Long>,
    @JsonProperty("senders")
    val senders: List<Long>,
    @JsonProperty("groupNames")
    val groupNames: List<String>? = emptyList()
)
