package com.emailmarketingtool.models.requests

data class UpdateSenderRequest(
    val senderId: Long,
    var mail: String? = null,
    var port: Int? = null,
    var host: String? = null,
    var passKey: String? = null,
    var signatures: List<String>? = null,
    var userId: Long? = null
){
    init {
        host = host?.trim()
    }
}