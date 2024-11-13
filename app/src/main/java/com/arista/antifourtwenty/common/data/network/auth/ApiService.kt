package com.arista.antifourtwenty.common.data.network.auth

import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("anti_login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}