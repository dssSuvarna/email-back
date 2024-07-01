package com.emailmarketingtool.models.requests

data class EmailRequest(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val contacts: List<String>,
    val senders: List<String>,
)