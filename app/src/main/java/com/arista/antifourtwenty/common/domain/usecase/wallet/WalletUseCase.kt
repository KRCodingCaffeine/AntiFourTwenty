package com.arista.antifourtwenty.common.domain.usecase.wallet

import com.arista.antifourtwenty.common.domain.models.wallet.WalletRequest
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponse

interface WalletUseCase {
    suspend fun execute(request: WalletRequest): Result<WalletResponse>
}