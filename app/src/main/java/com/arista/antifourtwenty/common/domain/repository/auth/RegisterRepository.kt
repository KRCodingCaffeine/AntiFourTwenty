package com.arista.antifourtwenty.common.domain.repository.auth

import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterRequest
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterResponse

interface RegisterRepository {
    suspend fun register(request: RegisterRequest) : Result<RegisterResponse>
}