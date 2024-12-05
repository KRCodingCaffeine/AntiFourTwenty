package com.arista.antifourtwenty.features.upgradeapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppRequest
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppResponse
import com.arista.antifourtwenty.common.domain.usecase.upgradeapp.UpgradeAppUseCase
import kotlinx.coroutines.launch

class UpgradeAppViewModel(
    private val upgradeAppUseCase: UpgradeAppUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager
) : ViewModel(){
    val upgradeAppResponse = MutableLiveData<UpgradeAppResponse>()
    val errorMessage = MutableLiveData<String>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun upgradeApp(userKey: String) {
        Log.d("Anti", "Login")
        val request = UpgradeAppRequest(sharedPreferenceManager.getUserId().toString(), "2", userKey)
        viewModelScope.launch {
            _loading.value = true
            val result = upgradeAppUseCase.execute(request)
            result.fold(
                onSuccess = { response ->
                    when (response) {
                        is UpgradeAppResponse.Success -> {
                            // Handle successful login
                            val userDetails = response.message
                           sharedPreferenceManager.setWalletBalance(userDetails.wallet_balance)
                            sharedPreferenceManager.setAppCategory(userDetails.app_category)
                        }

                        is UpgradeAppResponse.Error -> {
                            // Handle login error
                            Log.d("anti", "Error")
                        }

                        else -> {
                            Log.d("anti", "Error")
                        }
                    }
                    upgradeAppResponse.postValue(response)
                    _loading.value = false
                },
                onFailure = { error ->
                    errorMessage.postValue(error.message)
                    _loading.value = false
                }
            )
        }
    }
}