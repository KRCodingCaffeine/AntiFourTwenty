package com.arista.antifourtwenty.common.domain.models.wallet

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class WalletResponse {
    @JsonClass(generateAdapter = true)
    data class Success(

        val code: Int,
        val status: Boolean,
        val message: WalletDetails
    ) : WalletResponse()

    @JsonClass(generateAdapter = true)
    data class Error(
        val code: Int,
        val status: Boolean,
        val message: String
    ) : WalletResponse()

    @JsonClass(generateAdapter = true)
    data class WalletDetails(
        @Json(name = "rumi_user_id") val rumi_user_id: String,
        @Json(name = "wallet_balance") val wallet_balance: Long,
    )
}