package com.arista.antifourtwenty.features.call.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arista.antifourtwenty.common.domain.usecase.twilio.TwilioUseCase
import kotlinx.coroutines.launch
import kotlin.random.Random

class CallActivityViewModel(
    private val twilioUseCase: TwilioUseCase
): ViewModel() {
    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> get() = _accessToken
    // List of Twilio numbers
    private val twilioNumbers = listOf("+16316145120", "+12675891939", "+13133074255")

    // Index to keep track of the last used number
    private var lastUsedIndex = 0
    fun generateToken(){
        viewModelScope.launch {
            val result = twilioUseCase.execute()
            result.fold(
                onSuccess = { response ->
                    Log.d("anti", response.toString())
                    _accessToken.value = response.token
                },
                onFailure = { error ->
                    Log.d("anti", "error")
                }

            )
        }
    }

    fun getNextTwilioNumber(): String {
        // Get the next number from the list
      /*  val nextNumber = twilioNumbers[lastUsedIndex]

        // Update the index for the next call
        lastUsedIndex = (lastUsedIndex + 1) % twilioNumbers.size
        return nextNumber
*/
        val randomIndex = Random.nextInt(twilioNumbers.size)
        // Return the Twilio number at the random index
        return twilioNumbers[randomIndex]

    }
}