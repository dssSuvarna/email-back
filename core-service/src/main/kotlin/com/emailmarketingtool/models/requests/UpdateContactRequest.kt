package com.emailmarketingtool.models.requests


data class UpdateContactRequest(
    var contactId: Long,
    var name: String? = null,
    var email: String? = null,
    var group: String? = null,
    var company: String? = null
)