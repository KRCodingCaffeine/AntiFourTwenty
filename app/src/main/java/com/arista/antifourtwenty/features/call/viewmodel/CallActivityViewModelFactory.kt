package com.arista.antifourtwenty.features.call.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arista.antifourtwenty.common.domain.usecase.twilio.TwilioUseCase

class CallActivityViewModelFactory(
    private val twilioUseCase: TwilioUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CallActivityViewModel::class.java)) {
            return CallActivityViewModel(twilioUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}