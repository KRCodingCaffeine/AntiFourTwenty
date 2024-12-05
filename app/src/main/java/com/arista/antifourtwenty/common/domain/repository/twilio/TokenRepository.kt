package com.arista.antifourtwenty.common.domain.repository.twilio

import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterRequest
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterResponse
import com.arista.antifourtwenty.common.domain.models.twilio.TokenResponse

interface TokenRepository {
    suspend fun generateToken() : Result<TokenResponse>
}