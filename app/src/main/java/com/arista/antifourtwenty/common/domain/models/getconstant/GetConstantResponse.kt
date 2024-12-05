package com.arista.antifourtwenty.common.domain.models.getconstant

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class GetConstantResponse {

    @JsonClass(generateAdapter = true)
    data class Success(
        val code: Int,
        val status: Boolean,
        val message: Message
    ) : GetConstantResponse()

    @JsonClass(generateAdapter = true)
    data class Error(
        val code: Int,
        val status: Boolean,
        val message: String
    ) : GetConstantResponse()

    @JsonClass(generateAdapter = true)
    data class Message(
        @Json(name = "api_base_url") val apiBaseUrl: String,
        @Json(name = "android_version") val androidVersion: Double,
        @Json(name = "iphone_version") val iphoneVersion: Int,
        @Json(name = "user") val user: UserDetails
    )

    @JsonClass(generateAdapter = true)
    data class UserDetails(
        @Json(name = "rumi_user_id") val rumiUserId: Int,
        @Json(name = "name") val name: String,
        @Json(name = "email") val email: String,
        @Json(name = "mobile") val mobile: Long,
        @Json(name = "wallet_balance") val walletBalance: Long,
        @Json(name = "app_category") val appCategory: String
    )
}
