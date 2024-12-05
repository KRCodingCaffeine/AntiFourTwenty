package com.arista.antifourtwenty.common.domain.repository.wallet

import com.arista.antifourtwenty.common.domain.models.wallet.WalletRequest
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponse

interface WalletRepository {
    suspend fun updateWallet(request: WalletRequest): Result<WalletResponse>
}