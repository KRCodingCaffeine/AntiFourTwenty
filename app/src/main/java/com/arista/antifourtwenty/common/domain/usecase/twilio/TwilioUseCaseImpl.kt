package com.arista.antifourtwenty.common.domain.usecase.twilio

import com.arista.antifourtwenty.common.domain.models.twilio.TokenResponse
import com.arista.antifourtwenty.common.domain.repository.twilio.TokenRepository

class TwilioUseCaseImpl(private val tokenRepository: TokenRepository) : TwilioUseCase {
    override suspend fun execute(): Result<TokenResponse> {
        return tokenRepository.generateToken()
    }
}