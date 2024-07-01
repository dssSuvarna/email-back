package com.emailmarketingtool.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class OtpVerificationResponse(
    @JsonProperty("response")
    val response: String,
    @JsonProperty("token")
    val token: String
)
