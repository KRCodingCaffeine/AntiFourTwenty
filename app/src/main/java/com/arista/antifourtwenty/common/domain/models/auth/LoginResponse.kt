package com.arista.antifourtwenty.common.domain.models.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class LoginResponse {
    @JsonClass(generateAdapter = true)
    data class Success(
        val code: Int,
        val status: Boolean,
        val message: UserDetails
    ) : LoginResponse()

    @JsonClass(generateAdapter = true)
    data class Error(
        val code: Int,
        val status: Boolean,
        val message: String
    ) : LoginResponse()

    @JsonClass(generateAdapter = true)
    data class UserDetails(
        @Json(name = "rumi_user_id") val rumiUserId: Int,
        @Json(name = "name") val name: String,
        @Json(name = "email") val email: String,
        @Json(name = "mobile") val mobile: Long
    )
}