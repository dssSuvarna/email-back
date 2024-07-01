package com.emailmarketingtool.models.responses

data class TemplateResponse (
    val id: Long,
    val name: String,
    var content: String
)