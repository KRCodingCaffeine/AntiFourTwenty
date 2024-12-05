package com.arista.antifourtwenty.common.domain.repository.upgradeapp

import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppRequest
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppResponse

interface UpgradeAppRepository {
    suspend fun upgradeApp(request: UpgradeAppRequest): Result<UpgradeAppResponse>
}