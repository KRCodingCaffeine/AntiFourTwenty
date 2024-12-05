package com.arista.antifourtwenty.common.domain.models.auth

import android.util.Log
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.io.IOException

class LoginResponseAdapter(moshi: Moshi) {
    private val userDetailsAdapter: JsonAdapter<LoginResponse.UserDetails> = moshi.adapter(LoginResponse.UserDetails::class.java)
    @FromJson
    fun fromJson(reader: JsonReader): LoginResponse {
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
                        message = userDetailsAdapter.fromJson(reader)
                    } else {
                        message = reader.nextString()
                    }
                }
            }
        }
        reader.endObject()

        return when (message) {
            is LoginResponse.UserDetails -> LoginResponse.Success(code, status, message)
            is String -> LoginResponse.Error(code, status, message)
            else -> throw JsonDataException("Unexpected message type: $message")
        }
    }

    @ToJson
    fun toJson(response: LoginResponse): Map<String, Any> {
        return when (response) {
            is LoginResponse.Success -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to response.message
            )
            is LoginResponse.Error -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to response.message
            )
        }
    }
}