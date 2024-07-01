package com.emailmarketingtool.models.responses

data class ContactResponse(
    val id: Long,
    val name: String? = null,
    val email: String,
    val company: String? = null,
    val group: String,
    val isOpened: Boolean
)