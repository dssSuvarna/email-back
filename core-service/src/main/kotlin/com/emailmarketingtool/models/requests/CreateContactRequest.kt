package com.emailmarketingtool.models.requests

import java.util.regex.Pattern


data class CreateContactRequest(
    var name: String? = null,
    var email: String,
    var company: String? = null,
    var group: String
) {
    init {
        require(email.isNotBlank()) { "Email cannot be null" }
        require(group.isNotBlank()) { "Group cannot be null" }
        require(isValidEmail(email)) { "Invalid email format" }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"
        val pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE)
        return pattern.matcher(email).matches()
    }
}