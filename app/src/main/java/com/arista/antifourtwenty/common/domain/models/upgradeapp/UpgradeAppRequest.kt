package com.arista.antifourtwenty.common.domain.models.upgradeapp

data class UpgradeAppRequest (
    val rumi_user_id: String,
    val abaci_app_id: String,
    val user_key: String,
)