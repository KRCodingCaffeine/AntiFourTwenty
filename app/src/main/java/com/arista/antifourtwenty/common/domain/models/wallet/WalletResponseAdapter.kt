package com.arista.antifourtwenty.common.domain.models.wallet

import android.util.Log
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson

class WalletResponseAdapter(moshi: Moshi) {
    private val walletDetailsAdapter: JsonAdapter<WalletResponse.WalletDetails> = moshi.adapter(
        WalletResponse.WalletDetails::class.java)
    @FromJson
    fun fromJson(reader: JsonReader): WalletResponse {
        reader.beginObject()
        Log.d("anti", reader.toString())
        var code = 0
        var status = false
        var message: Any? = null
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "code" -> code = reader.nextInt()
                "status" -> status = reader.nextBoolean()
                "message" -> {
                    if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
                        message = walletDetailsAdapter.fromJson(reader)
                    } else {
                        message = reader.nextString()
                    }
                }
            }
        }
        reader.endObject()

        return when (message) {
            is WalletResponse.WalletDetails -> WalletResponse.Success(code, status, message)
            is String -> WalletResponse.Error(code, status, message)
            else -> throw JsonDataException("Unexpected message type: $message")
        }
    }

    @ToJson
    fun toJson(response: WalletResponse): Map<String, Any> {
        return when (response) {
            is WalletResponse.Success -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to response.message
            )
            is WalletResponse.Error -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to response.message
            )
        }
    }
}