package com.emailmarketingtool.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class Response(
    @JsonProperty("response")
    val response: String
)