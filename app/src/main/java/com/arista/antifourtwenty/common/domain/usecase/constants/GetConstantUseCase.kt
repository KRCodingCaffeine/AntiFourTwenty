package com.arista.antifourtwenty.common.domain.usecase.constants

import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantRequest
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantResponse

interface GetConstantUseCase {
    suspend fun execute(request: GetConstantRequest): Result<GetConstantResponse>
}
