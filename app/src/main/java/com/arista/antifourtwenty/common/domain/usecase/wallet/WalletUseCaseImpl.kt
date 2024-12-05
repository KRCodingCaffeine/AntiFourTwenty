package com.arista.antifourtwenty.common.domain.usecase.wallet

import com.arista.antifourtwenty.common.domain.models.wallet.WalletRequest
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponse
import com.arista.antifourtwenty.common.domain.repository.twilio.TokenRepository
import com.arista.antifourtwenty.common.domain.repository.wallet.WalletRepository

class WalletUseCaseImpl(private val walletRepository: WalletRepository) : WalletUseCase {
    override suspend fun execute(request: WalletRequest): Result<WalletResponse> {
        return walletRepository.updateWallet(request)
    }
}