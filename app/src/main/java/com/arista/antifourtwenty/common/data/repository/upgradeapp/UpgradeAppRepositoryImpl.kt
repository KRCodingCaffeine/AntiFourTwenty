package com.arista.antifourtwenty.common.data.repository.upgradeapp

import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppRequest
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppResponse
import com.arista.antifourtwenty.common.domain.repository.auth.LoginRepository
import com.arista.antifourtwenty.common.domain.repository.upgradeapp.UpgradeAppRepository

class UpgradeAppRepositoryImpl (private val apiService: ApiService) : UpgradeAppRepository {
    override suspend fun upgradeApp(request: UpgradeAppRequest): Result<UpgradeAppResponse> {
        return try {
            val response = apiService.upgradeAppCategory(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Login failed"))
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}