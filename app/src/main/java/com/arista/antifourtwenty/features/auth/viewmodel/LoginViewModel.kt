package com.arista.antifourtwenty.features.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    val loginResponse = MutableLiveData<LoginResponse>()
    val errorMessage = MutableLiveData<String>()

    fun login(username: String, userKey: String) {
        val request = LoginRequest(username, "2", userKey)
        viewModelScope.launch {
            val result = loginUseCase.execute(request)
            result.fold(
                onSuccess = { response ->
                    loginResponse.postValue(response)
                },
                onFailure = { error ->
                    errorMessage.postValue(error.message)
                }
            )
        }
    }
}