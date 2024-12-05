package com.arista.antifourtwenty.common.domain.usecase.constants

import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantRequest
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantResponse
import com.arista.antifourtwenty.common.domain.repository.constants.GetConstantsRepository

class GetConstantUseCaseImpl(private val getConstantsRepository: GetConstantsRepository) : GetConstantUseCase {
    override suspend fun execute(request: GetConstantRequest): Result<GetConstantResponse> {
        return getConstantsRepository.getConstants(request)
    }
}
