package com.arista.antifourtwenty.common.domain.usecase.auth

import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.repository.auth.LoginRepository

class LoginUseCaseImpl(private val loginRepository: LoginRepository) : LoginUseCase {
    override suspend fun execute(request: LoginRequest): Result<LoginResponse> {
        return loginRepository.login(request)
    }
}