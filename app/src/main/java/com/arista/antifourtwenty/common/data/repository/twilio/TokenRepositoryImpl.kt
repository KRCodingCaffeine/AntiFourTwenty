package com.arista.antifourtwenty.common.data.repository.twilio

import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.domain.models.twilio.TokenResponse
import com.arista.antifourtwenty.common.domain.repository.twilio.TokenRepository

class TokenRepositoryImpl(private val apiService: ApiService) : TokenRepository {
    override suspend fun generateToken(): Result<TokenResponse> {
        return try{
            val response = apiService.generateToken()
            if(response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Generate Token Failed"))
            }else{
                Result.failure(Exception("Generate Token Failed"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}