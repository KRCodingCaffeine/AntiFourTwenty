package com.arista.antifourtwenty.common.domain.models.upgradeapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class UpgradeAppResponse {
    @JsonClass(generateAdapter = true)
    data class Success(
        val code: Int,
        val status: Boolean,
        val message: AppDetails
    ) : UpgradeAppResponse()

    @JsonClass(generateAdapter = true)
    data class Error(
        val code: Int,
        val status: Boolean,
        val message: String
    ) : UpgradeAppResponse()

    @JsonClass(generateAdapter = true)
    data class AppDetails(
        @Json(name = "wallet_balance") val wallet_balance: Long,
        @Json(name = "app_category") val app_category: String
    )
}