package com.arista.antifourtwenty.features.history.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arista.antifourtwenty.common.data.local.database.entities.CallLog
import com.arista.antifourtwenty.common.domain.usecase.calllogs.CallLogUseCase
import kotlinx.coroutines.launch

class CallLogViewModel(private val callLogUsecase: CallLogUseCase) : ViewModel() {

    private val _callLogs = MutableLiveData<List<CallLog>>()
    val callLogs: LiveData<List<CallLog>> get() = _callLogs

    fun fetchCallLogs() {
        viewModelScope.launch {
            _callLogs.value = callLogUsecase.getAllCallLogs()
        }
    }

}