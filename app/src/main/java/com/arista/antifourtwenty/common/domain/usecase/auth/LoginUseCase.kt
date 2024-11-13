package com.arista.antifourtwenty.common.domain.usecase.auth

import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse

interface LoginUseCase {
    suspend fun execute(request: LoginRequest): Result<LoginResponse>
}