package com.arista.antifourtwenty.common.data.network

import com.arista.antifourtwenty.common.domain.models.auth.LoginResponseAdapter
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterResponseAdapter
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantResponseAdapter
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppResponseAdapter
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponseAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient

object RetrofitClient {
    private const val BASE_URL = "https://www.krcodingcaffeine.com/antifourtwenty/Admin_api/"
    //private const val BASE_URL = "https://twilio.developerbrothersproject.com/api/"
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(LoginResponseAdapter(Moshi.Builder().build()))
            .add(RegisterResponseAdapter(Moshi.Builder().build()))
            .add(GetConstantResponseAdapter(Moshi.Builder().build()))
            .add(WalletResponseAdapter(Moshi.Builder().build()))
            .add(UpgradeAppResponseAdapter(Moshi.Builder().build()))
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}