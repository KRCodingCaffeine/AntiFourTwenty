package com.arista.antifourtwenty.features.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterRequest
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterResponse
import com.arista.antifourtwenty.common.domain.usecase.auth.LoginUseCase
import com.arista.antifourtwenty.common.domain.usecase.auth.register.RegisterUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager
) : ViewModel() {
    val loginResponse = MutableLiveData<LoginResponse>()
    val errorMessage = MutableLiveData<String>()

    val registerResponse = MutableLiveData<RegisterResponse>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun login(username: String, userKey: String) {
        Log.d("Anti", "Login")
        val request = LoginRequest(username, "2", userKey)
        viewModelScope.launch {
            _loading.value = true
            val result = loginUseCase.execute(request)
            result.fold(
                onSuccess = { response ->
                    when (response) {
                        is LoginResponse.Success -> {
                            // Handle successful login
                            val userDetails = response.message
                            sharedPreferenceManager.setLoggedIn(true)
                            sharedPreferenceManager.saveUserDetails(
                                userId = userDetails.rumiUserId,
                                name = userDetails.name,
                                email = userDetails.email,
                                mobile = userDetails.mobile
                            )
                        }

                        is LoginResponse.Error -> {
                            // Handle login error
                            Log.d("anti", "Error")
                        }

                        else -> {
                            Log.d("anti", "Error")
                        }
                    }
                    loginResponse.postValue(response)
                    _loading.value = false
                },
                onFailure = { error ->
                    errorMessage.postValue(error.message)
                    _loading.value = false
                }
            )
        }
    }

    fun register(username: String, mobile: String, email:String) {
        Log.d("Anti", "Login")
        val request = RegisterRequest(username, mobile, email, "2")
        viewModelScope.launch {
            _loading.value = true
            val result = registerUseCase.execute(request)
            result.fold(
                onSuccess = { response ->
                    when (response) {
                        is RegisterResponse.Success -> {
                            // Handle successful login
                            val userDetails = response.message
                            sharedPreferenceManager.setLoggedIn(true)
                            sharedPreferenceManager.saveUserDetails(
                                userId = userDetails.id,
                                name = userDetails.name,
                                email = userDetails.email,
                                mobile = userDetails.mobile
                            )
                        }

                        is RegisterResponse.Error -> {
                            // Handle login error
                            Log.d("anti", "Error")
                        }

                        else -> {
                            Log.d("anti", "Error")
                        }
                    }
                    registerResponse.postValue(response)
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