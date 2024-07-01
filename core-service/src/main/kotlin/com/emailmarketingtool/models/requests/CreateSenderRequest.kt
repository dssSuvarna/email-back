package com.emailmarketingtool.models.requests


data class CreateSenderRequest(
    var mail: String,
    var port: Int,
    var host: String,
    var passKey: String,
    var signatures: List<String>,
    var userId: Long
) {
    init {
        host = host.trim()
    }
}
