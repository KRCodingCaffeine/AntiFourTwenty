package com.arista.antifourtwenty.common.domain.models.auth.register

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class RegisterResponse {
    @JsonClass(generateAdapter = true)
    data class Success(
        val code: Int,
        val status: Boolean,
        val message: UserRegDetails
    ) : RegisterResponse()

    @JsonClass(generateAdapter = true)
    data class Error(
        val code: Int,
        val status: Boolean,
        val message: String
    ) : RegisterResponse()

    @JsonClass(generateAdapter = true)
    data class UserRegDetails(
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "email") val email: String,
        @Json(name = "mobile") val mobile: Long,
        @Json(name = "wallet_balance") val wallet_balance: Long,
        @Json(name = "app_category") val app_category: String,
    )
    /*{
        "code": 9,
        "status": true,
        "message": {
        "id": 4524,
        "name": "Karthika",
        "email": "krcoding@gmail.com",
        "mobile": 8928290341,
        "wallet_balance": 3,
        "app_category": "base"
    }
    }*/


}