package com.arista.antifourtwenty.common.data.network.auth

import com.arista.antifourtwenty.common.domain.models.auth.LoginRequest
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterRequest
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterResponse
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantRequest
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantResponse
import com.arista.antifourtwenty.common.domain.models.twilio.TokenResponse
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppRequest
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppResponse
import com.arista.antifourtwenty.common.domain.models.wallet.WalletRequest
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("anti_login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("anti_register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("generate-token")
    suspend fun generateToken(): Response<TokenResponse>

    @POST("anti_constants")
    suspend fun getConstant(@Body request: GetConstantRequest): Response<GetConstantResponse>

    @POST("update_wallet")
    suspend fun updateWallet(@Body request: WalletRequest): Response<WalletResponse>

    @POST("upgrade_app")
    suspend fun upgradeApp(@Body request: UpgradeAppRequest): Response<UpgradeAppResponse>

    @POST("upgrade_app")
    suspend fun upgradeAppCategory(@Body request: UpgradeAppRequest):Response<UpgradeAppResponse>
}