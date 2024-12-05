package com.arista.antifourtwenty.common.data.repository.constants

import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantRequest
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantResponse
import com.arista.antifourtwenty.common.domain.repository.auth.LoginRepository
import com.arista.antifourtwenty.common.domain.repository.constants.GetConstantsRepository

class ConstantRepositoryImpl(private val apiService: ApiService) : GetConstantsRepository {
    override suspend fun getConstants(request: GetConstantRequest): Result<GetConstantResponse> {
        return try{
            val response = apiService.getConstant(request)
            if(response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Get constant failed"))
            }else{
                Result.failure(Exception("Get constant failed"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}