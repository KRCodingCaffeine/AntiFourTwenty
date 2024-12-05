package com.arista.antifourtwenty.common.data.repository.auth

import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterRequest
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterResponse
import com.arista.antifourtwenty.common.domain.repository.auth.RegisterRepository

class RegisterRepositoryImpl(private val apiService: ApiService) : RegisterRepository {
    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = apiService.register(request)
            if(response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Register Failed"))
            }else{
                Result.failure(Exception("Register Failed"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}