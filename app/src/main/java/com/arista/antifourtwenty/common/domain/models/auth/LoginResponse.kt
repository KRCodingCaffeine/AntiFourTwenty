package com.arista.antifourtwenty.common.domain.models.auth

sealed class LoginResponse {
    data class Success(
        val code: Int,
        val status: Boolean,
        val message: UserDetails
    ) : LoginResponse()

    data class Error(
        val code: Int,
        val status: Boolean,
        val message: String
    ) : LoginResponse()

    data class UserDetails(
        val rumiUserId: Int,
        val name: String,
        val email: String,
        val mobile: Long
    )
}