package com.arista.antifourtwenty.features.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.domain.usecase.constants.GetConstantUseCase
import com.arista.antifourtwenty.common.domain.usecase.twilio.TwilioUseCase
import com.arista.antifourtwenty.common.domain.usecase.wallet.WalletUseCase
import com.arista.antifourtwenty.common.utils.PermissionManager
import com.arista.antifourtwenty.features.auth.viewmodel.LoginViewModel

class MainActivityViewModelFactory(
    private val permissionManager: PermissionManager,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val twilioUseCase: TwilioUseCase,
    private val getConstantUseCase: GetConstantUseCase,
    private val walletUseCase: WalletUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(permissionManager, sharedPreferenceManager, twilioUseCase, getConstantUseCase,walletUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}