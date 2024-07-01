package com.emailmarketingtool.errors

class ErrorMessages {
    companion object {
        const val INVALID_EMAIL = "Invalid email"
        const val USER_NOT_FOUND = "User not found"
        const val AUTH_USER_NOT_FOUND = "Auth user not found"
        const val AUTH_USER_ALREADY_REGISTERED = "Auth user already registered"
        const val INVALID_CREDENTIALS = "Invalid Credentials"
        const val INVITE_ERROR = "User has been {status}"
        const val INVALID_ACCESS = "Invalid access"
        const val INVALID_TOKEN = "Invalid token"
        const val RESOURCE_NOT_FOUND = "Resource not found"
        const val NOT_ALLOWED = "Not Allowed to perform current action"
        const val FILE_SIZE_SHOULD_BE_LESS_THAN = "File size should be less than "
        const val INVALID_OTP = "Invalid OTP"
        const val MISMATCH_PASSWORD = "Password not matched"
        const val INVALID_STATUS = "Unknown status"
        const val SENDER_NOT_ASSOCIATED_WITH_USER = "Sender is not associated with logged in user: "
        const val SENDER_NOT_FOUND = "Sender not found with this id: "
        const val MISS_MATCH_USER = "Given user id is not matching with logged in user"
    }
}
