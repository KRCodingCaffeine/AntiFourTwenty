package com.arista.antifourtwenty.features.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.utils.PermissionManager
import android.Manifest
import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantRequest
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantResponse
import com.arista.antifourtwenty.common.domain.models.wallet.WalletRequest
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponse
import com.arista.antifourtwenty.common.domain.usecase.constants.GetConstantUseCase
import com.arista.antifourtwenty.common.domain.usecase.twilio.TwilioUseCase
import com.arista.antifourtwenty.common.domain.usecase.wallet.WalletUseCase
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val permissionManager: PermissionManager,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val twilioUseCase: TwilioUseCase,
    private val getConstantUseCase: GetConstantUseCase,
    private val walletUseCase: WalletUseCase,
) : ViewModel() {
    private val _permissionsGranted = MutableLiveData<Boolean>()
    val permissionsGranted: LiveData<Boolean> get() = _permissionsGranted

    val requiredPermissions = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.RECORD_AUDIO
    )

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    val getConstantResponse = MutableLiveData<GetConstantResponse>()
    val errorMessage = MutableLiveData<String>()

    private val _walletResponse = MutableLiveData<WalletResponse>()
    val walletResponse: LiveData<WalletResponse> get() = _walletResponse

    fun checkPermission(activity: Activity): Boolean {
        if (permissionManager.arePermissionsGranted(requiredPermissions)) {
            return true
        }
        return false
    }

    fun checkAndRequestPermissions(activity: Activity) {
        permissionManager.requestPermissions(activity, requiredPermissions, REQUEST_CODE)
    }

    fun onPermissionsResult(allPermissionsGranted: Boolean) {
        _permissionsGranted.value = allPermissionsGranted
    }

    fun generateToken() {
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
    }

    fun getConstants() {
        //val request = GetConstantRequest(sharedPreferenceManager.getUserId().toString())
        val request = GetConstantRequest(sharedPreferenceManager.getUserId().toString())
        viewModelScope.launch {
            _loading.value = true
            val result = getConstantUseCase.execute(request)
            result.fold(
                onSuccess = { response ->
                    when (response) {
                        is GetConstantResponse.Success -> {
                            // Handle successful login
                            val userDetails = response.message
                            sharedPreferenceManager.saveUserWalletDetails(
                                userId = userDetails.user.rumiUserId,
                                name = userDetails.user.name,
                                email = userDetails.user.email,
                                mobile = userDetails.user.mobile,
                                walletBalance = userDetails.user.walletBalance,
                                appCategory = userDetails.user.appCategory
                            )

                        }

                        is GetConstantResponse.Error -> {
                            // Handle login error
                            Log.d("anti", "Error")
                        }
                    }
                    getConstantResponse.value = response
                    _loading.value = false
                },
                onFailure = { error ->
                    errorMessage.postValue(error.message)
                    _loading.value = false
                }
            )
        }
    }

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

    fun logout(){
        sharedPreferenceManager.setLoggedIn(false)
        sharedPreferenceManager.clearLoginState()
    }

    companion object {
        const val REQUEST_CODE = 101
    }

}