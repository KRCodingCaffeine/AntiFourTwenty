package com.arista.antifourtwenty.common.domain.repository.constants

import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantRequest
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantResponse

interface GetConstantsRepository {
    suspend fun getConstants(request: GetConstantRequest): Result<GetConstantResponse>
}