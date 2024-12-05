package com.arista.antifourtwenty.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.wallet.WalletRequest
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponse
import com.arista.antifourtwenty.common.domain.usecase.calllogs.CallLogUseCase
import com.arista.antifourtwenty.common.domain.usecase.wallet.WalletUseCase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeViewModel(
    private val walletUseCase:WalletUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val callLogUsecase: CallLogUseCase,
) : ViewModel(){
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading
    private val _walletResponse = MutableLiveData<WalletResponse>()
    val walletResponse: LiveData<WalletResponse> get() = _walletResponse
    fun updateWallet(walletBalance: String) {
        val request = WalletRequest(sharedPreferenceManager.getUserId().toString(),  "2", walletBalance)
        viewModelScope.launch {
            _loading.value = true
            val result = walletUseCase.execute(request)
            result.fold(
                onSuccess = { response ->
                    when (response) {
                        is WalletResponse.Success -> {
                            val walletDetails = response.message
                            sharedPreferenceManager.setWalletBalance(walletDetails.wallet_balance)
                            _walletResponse.postValue(response)
                        }

                        is WalletResponse.Error -> {
                            // Handle login error
                            Log.d("anti", "Error")
                            _walletResponse.postValue(response)
                        }

                        else -> {
                            Log.d("anti", "Error")
                            _walletResponse.postValue(response)
                        }
                    }
                    _loading.value = false
                },
                onFailure = { error ->

                    _walletResponse.postValue(WalletResponse.Error(0,false, error.message.toString()))
                    _loading.value = false

                }
            )
        }
    }

    fun addCallLogs(mobile : String) {
        viewModelScope.launch {
            val logID = callLogUsecase.addCallLog(
                mobile = mobile, date = getCurrentDate(), time = getCurrentTime()
            )
            Log.d("Anti data", logID.toString())
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return formatter.format(calendar.time)
    }

   /* fun generateToken() {
        viewModelScope.launch {
            val result = twilioUseCase.execute()
            result.fold(
                onSuccess = { response ->
                    Log.d("anti", response.toString())
                    sharedPreferenceManager.setTwilioToken(response.token)
                },
                onFailure = { error ->
                    Log.d("anti", "error")
                }

            )
        }
    }*/

}