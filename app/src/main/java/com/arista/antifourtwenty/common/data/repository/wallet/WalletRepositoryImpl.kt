package com.arista.antifourtwenty.common.data.repository.wallet

import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.domain.models.wallet.WalletRequest
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponse
import com.arista.antifourtwenty.common.domain.repository.wallet.WalletRepository

class WalletRepositoryImpl(private val apiService: ApiService) : WalletRepository {
    override suspend fun updateWallet(request: WalletRequest): Result<WalletResponse> {
        return try{
            val response = apiService.updateWallet(request)
            if(response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Failed to update wallet balance"))
            }else{
                Result.failure(Exception("Failed to update wallet balance"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}