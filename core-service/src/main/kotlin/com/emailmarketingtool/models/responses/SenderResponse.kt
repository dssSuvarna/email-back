package com.emailmarketingtool.models.responses

data class SenderResponse(
    val senderId: Long,
    var mail: String,
    var port: Int,
    var host: String,
    var signatures: List<String>,
    var userId: Long,
    var passKey: String,
)