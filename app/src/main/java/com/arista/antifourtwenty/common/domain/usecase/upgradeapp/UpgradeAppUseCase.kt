package com.arista.antifourtwenty.common.domain.usecase.upgradeapp

import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppRequest
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppResponse

interface UpgradeAppUseCase {
    suspend fun execute(request: UpgradeAppRequest): Result<UpgradeAppResponse>
}