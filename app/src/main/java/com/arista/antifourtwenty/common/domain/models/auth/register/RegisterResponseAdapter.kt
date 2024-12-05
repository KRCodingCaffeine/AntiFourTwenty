package com.arista.antifourtwenty.common.domain.models.auth.register

import android.util.Log
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson

class RegisterResponseAdapter(moshi: Moshi) {
    private val userRegDetailsAdapter: JsonAdapter<RegisterResponse.UserRegDetails> = moshi.adapter(RegisterResponse.UserRegDetails::class.java)
    @FromJson
    fun fromJson(reader: JsonReader): RegisterResponse {
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
                        message = userRegDetailsAdapter.fromJson(reader)
                    } else {
                        message = reader.nextString()
                    }
                }
            }
        }
        reader.endObject()

        return when (message) {
            is RegisterResponse.UserRegDetails-> RegisterResponse.Success(code, status, message)
            is String -> RegisterResponse.Error(code, status, message)
            else -> throw JsonDataException("Unexpected message type: $message")
        }
    }

    @ToJson
    fun toJson(response: RegisterResponse): Map<String, Any> {
        return when (response) {
            is RegisterResponse.Success -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to response.message
            )
            is RegisterResponse.Error -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to response.message
            )
        }
    }
}
