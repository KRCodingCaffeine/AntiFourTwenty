package com.arista.antifourtwenty.common.domain.usecase.auth.register

import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterRequest
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterResponse
import com.arista.antifourtwenty.common.domain.repository.auth.RegisterRepository

class RegisterUseCaseImpl(private val registerRepository: RegisterRepository) : RegisterUseCase {
    override suspend fun execute(request: RegisterRequest): Result<RegisterResponse> {
        return registerRepository.register(request)
    }

}