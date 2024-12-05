package com.arista.antifourtwenty.common.domain.usecase.twilio

import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.twilio.TokenResponse

interface TwilioUseCase {
    suspend fun execute(): Result<TokenResponse>
}