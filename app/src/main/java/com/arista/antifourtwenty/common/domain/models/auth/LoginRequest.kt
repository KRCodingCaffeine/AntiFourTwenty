package com.arista.antifourtwenty.common.domain.models.auth

data class LoginRequest(
    val uName: String,
    val appID: String,
    val userKey: String,
)