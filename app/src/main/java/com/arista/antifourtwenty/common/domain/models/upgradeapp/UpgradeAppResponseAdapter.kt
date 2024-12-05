package com.arista.antifourtwenty.common.domain.models.upgradeapp

import android.util.Log
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson

class UpgradeAppResponseAdapter(moshi: Moshi) {
    private val appDetailsAdapter: JsonAdapter<UpgradeAppResponse.AppDetails> = moshi.adapter(UpgradeAppResponse.AppDetails::class.java)

    @FromJson
    fun fromJson(reader: JsonReader): UpgradeAppResponse {
        var code = 0
        var status = false
        var message: Any? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "code" -> code = reader.nextInt()
                "status" -> status = reader.nextBoolean()
                "message" -> {
                    message = if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
                        appDetailsAdapter.fromJson(reader)
                    } else {
                        reader.nextString()
                    }
                }
            }
        }
        reader.endObject()

        return when (message) {
            is UpgradeAppResponse.AppDetails -> UpgradeAppResponse.Success(code, status, message)
            is String -> UpgradeAppResponse.Error(code, status, message)
            else -> throw JsonDataException("Unexpected message type: $message")
        }
    }

    @ToJson
    fun toJson(response: UpgradeAppResponse): Map<String, Any> {
        return when (response) {
            is UpgradeAppResponse.Success -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to mapOf(
                    "wallet_balance" to response.message.wallet_balance,
                    "app_category" to response.message.app_category
                )
            )
            is UpgradeAppResponse.Error -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to response.message
            )
        }
    }
}