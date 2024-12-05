package com.arista.antifourtwenty.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.domain.usecase.auth.LoginUseCase
import com.arista.antifourtwenty.common.domain.usecase.calllogs.CallLogUseCase
import com.arista.antifourtwenty.common.domain.usecase.wallet.WalletUseCase
import com.arista.antifourtwenty.features.auth.viewmodel.LoginViewModel

class HomeViewModelFactory(
    private val walletUseCase: WalletUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val callLogUseCase: CallLogUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(walletUseCase, sharedPreferenceManager, callLogUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}