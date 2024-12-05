package com.arista.antifourtwenty.common.domain.models.auth.register

data class RegisterRequest (
    val uName: String,
    val uMobile: String,
    val uEmail: String,
    val appID: String,
)