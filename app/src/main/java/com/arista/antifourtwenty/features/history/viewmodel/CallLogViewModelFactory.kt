package com.arista.antifourtwenty.features.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arista.antifourtwenty.common.domain.usecase.calllogs.CallLogUseCase
import com.arista.antifourtwenty.features.viewmodel.MainActivityViewModel

class CallLogViewModelFactory(private val callLogUsecase: CallLogUseCase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CallLogViewModel::class.java)) {
            return CallLogViewModel(callLogUsecase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}