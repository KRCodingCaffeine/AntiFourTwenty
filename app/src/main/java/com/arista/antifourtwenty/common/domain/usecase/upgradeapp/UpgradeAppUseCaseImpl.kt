package com.arista.antifourtwenty.common.domain.usecase.upgradeapp

import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppRequest
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppResponse
import com.arista.antifourtwenty.common.domain.repository.upgradeapp.UpgradeAppRepository

class UpgradeAppUseCaseImpl(private val upgradeAppRepository: UpgradeAppRepository) : UpgradeAppUseCase {
    override suspend fun execute(request: UpgradeAppRequest): Result<UpgradeAppResponse> {
        return upgradeAppRepository.upgradeApp(request)
    }
}