package com.arista.antifourtwenty.common.domain.usecase.auth.register

import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterRequest
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterResponse

interface RegisterUseCase {
    suspend fun execute(request: RegisterRequest) : Result<RegisterResponse>
}