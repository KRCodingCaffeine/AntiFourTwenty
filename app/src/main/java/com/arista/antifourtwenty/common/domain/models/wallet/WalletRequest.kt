package com.arista.antifourtwenty.common.domain.models.wallet

data class WalletRequest (
    val rumi_user_id: String,
    val abaci_app_id: String,
    val wallet_balance: String,
)