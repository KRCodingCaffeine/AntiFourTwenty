package com.arista.antifourtwenty.common.domain.models.twilio

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    val token : String
)