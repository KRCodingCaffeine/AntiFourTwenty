package com.arista.antifourtwenty.common.domain.repository.auth

import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse

interface LoginRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
}