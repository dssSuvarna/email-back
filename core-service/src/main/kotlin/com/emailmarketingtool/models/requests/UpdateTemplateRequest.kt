package com.emailmarketingtool.models.requests


data class UpdateTemplateRequest(
    var templateId: Long,
    val content: String
)