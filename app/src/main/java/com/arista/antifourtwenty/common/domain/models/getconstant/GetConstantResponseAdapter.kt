package com.arista.antifourtwenty.common.domain.models.getconstant

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson

class GetConstantResponseAdapter(moshi: Moshi) {
    private val messageAdapter: JsonAdapter<GetConstantResponse.Message> = moshi.adapter(GetConstantResponse.Message::class.java)

    @FromJson
    fun fromJson(reader: JsonReader): GetConstantResponse {
        reader.beginObject()
        var code = 0
        var status = false
        var message: Any? = null

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "code" -> code = reader.nextInt()
                "status" -> status = reader.nextBoolean()
                "message" -> {
                    message = if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
                        messageAdapter.fromJson(reader)
                    } else {
                        reader.nextString()
                    }
                }
            }
        }
        reader.endObject()

        return when (message) {
            is GetConstantResponse.Message -> GetConstantResponse.Success(code, status, message)
            is String -> GetConstantResponse.Error(code, status, message)
            else -> throw JsonDataException("Unexpected message type: $message")
        }
    }

    @ToJson
    fun toJson(response: GetConstantResponse): Map<String, Any> {
        return when (response) {
            is GetConstantResponse.Success -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to response.message
            )
            is GetConstantResponse.Error -> mapOf(
                "code" to response.code,
                "status" to response.status,
                "message" to response.message
            )
        }
    }
}