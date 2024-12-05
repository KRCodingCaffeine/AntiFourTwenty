package com.arista.antifourtwenty.features.upgradeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.domain.usecase.upgradeapp.UpgradeAppUseCase
import com.arista.antifourtwenty.features.auth.viewmodel.LoginViewModel

class UpgradeAppViewModelFactory(
    private val upgradeAppUseCase: UpgradeAppUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpgradeAppViewModel::class.java)) {
            return UpgradeAppViewModel(upgradeAppUseCase, sharedPreferenceManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}