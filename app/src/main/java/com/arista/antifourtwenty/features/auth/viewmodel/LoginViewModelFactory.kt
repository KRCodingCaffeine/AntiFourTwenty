package com.arista.antifourtwenty.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.data.repository.auth.LoginRepositoryImpl
import com.arista.antifourtwenty.common.domain.usecase.auth.LoginUseCase
import com.arista.antifourtwenty.common.domain.usecase.auth.register.RegisterUseCase

class LoginViewModelFactory(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginUseCase, registerUseCase, sharedPreferenceManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}