package com.arista.antifourtwenty.features.call

import android.content.Context
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.data.network.RetrofitClient
import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.data.repository.twilio.TokenRepositoryImpl
import com.arista.antifourtwenty.common.domain.usecase.twilio.TwilioUseCaseImpl
import com.arista.antifourtwenty.databinding.ActivityCallBinding
import com.arista.antifourtwenty.features.call.viewmodel.CallActivityViewModel
import com.arista.antifourtwenty.features.call.viewmodel.CallActivityViewModelFactory
import com.twilio.voice.Call
import com.twilio.voice.CallException
import com.twilio.voice.ConnectOptions
import com.twilio.voice.Voice

class CallActivity : AppCompatActivity() {
    private val callActivityViewModel: CallActivityViewModel by viewModels {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val twilioRepositoryImpl = TokenRepositoryImpl(apiService)
        val twilioUseCase = TwilioUseCaseImpl(twilioRepositoryImpl)
        CallActivityViewModelFactory(twilioUseCase)
    }

    private lateinit var binding: ActivityCallBinding
    companion object{
        var call: Call? = null
    }
    private lateinit var mobileNo:String
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    var minutesRemaining: Int = 0
    var leftSeconds: Long = 0

    private lateinit var audioManager : AudioManager

    private var timer: CountDownTimer? = null
    private var maxTimeInMillis: Long = 0L

    private var handler: Handler? = null
    private var elapsedTimeInMillis: Long = 0L
    private  var walletBalance : Long = 0L
    private val costPerMinute: Long = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        supportActionBar?.hide()
        sharedPreferenceManager = SharedPreferenceManager(this)
        mobileNo = intent.getStringExtra("mobile").toString()
        binding.mobileNumber.text = mobileNo
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        walletBalance = sharedPreferenceManager.getWalletBalance()!!
        leftSeconds = 0
        maxTimeInMillis = calculateMaxTime()

        makeCall()
        binding.muteButton.setOnClickListener {
            call?.disconnect()
            finish()
        }
        binding.speakerButton.setOnClickListener {
            setSpeakerIcon()
        }
    }

    private fun calculateMaxTime(): Long {
        val maxMinutes = (sharedPreferenceManager.getWalletBalance()!!.toInt() / costPerMinute).toInt()
        return maxMinutes * 60 * 1000L // Convert minutes to milliseconds
    }

    fun setSpeakerIcon() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val isSpeakerOn = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
                .any { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }

            if (!isSpeakerOn) {
                // Enable speakerphone
                binding.speakerButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.mute_32
                    )
                )
                audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
                    .firstOrNull { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
                    ?.let { audioManager.setCommunicationDevice(it) }
            } else {
                // Disable speakerphone and revert to default audio device
                binding.speakerButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.speaker32
                    )
                )
                audioManager.clearCommunicationDevice()
            }
        }else {
            if (!audioManager.isSpeakerphoneOn) {
                binding.speakerButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.mute_32
                    )
                )
                audioManager.isSpeakerphoneOn = true
            } else {
                binding.speakerButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.speaker32
                    )
                )
                audioManager.isSpeakerphoneOn = false
            }
        }
    }

    private fun makeCall() {
        val fromNumber = callActivityViewModel.getNextTwilioNumber()
        val connectOptions = sharedPreferenceManager.getTwilioToken().let {
            ConnectOptions.Builder(it!!)
                .params(mapOf("To" to mobileNo, "From" to "+12675891939"))
                .build()
        }

        call = connectOptions.let { Voice.connect(applicationContext, it, callListener) }
    }

    private val callListener = object : Call.Listener {
        override fun onConnectFailure(call: Call, callException: CallException) {
            callException.printStackTrace()
            binding.callStatus.text = "Call Failed !!"
            Log.d("strVoice", callException.toString())
        }

        override fun onRinging(call: Call) {
            binding.callStatus.text = "Ringing..."
        }

        override fun onConnected(call: Call) {
            binding.callStatus.text = "Connected"
            binding.callButton.visibility = View.VISIBLE
            startCallTimer(maxTimeInMillis)
        }

        override fun onReconnecting(call: Call, callException: CallException) {
            TODO("Not yet implemented")
        }

        override fun onReconnected(call: Call) {
            TODO("Not yet implemented")
        }

        override fun onDisconnected(call: Call, error: CallException?) {
            binding.callStatus.text = "Call ended"
            stopCallTimer()
            val intent = Intent().apply {
                putExtra("final_balance", walletBalance.toString())
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun startCallTimer(maxTimeInMillis:Long) {
        handler = Handler(Looper.getMainLooper())
        elapsedTimeInMillis = 0L

        val runnable = object : Runnable {
            override fun run() {
                elapsedTimeInMillis += 1000
                val minutes = (elapsedTimeInMillis / 1000) / 60
                val seconds = (elapsedTimeInMillis / 1000) % 60
                binding.timing.text = String.format("%02d:%02d", minutes, seconds)

                if (seconds == 0L && minutes > 0) {
                    walletBalance -= costPerMinute
                    if (walletBalance <= 0) {
                        stopCallTimer()
                        binding.callStatus.text = "Call ended due to insufficient balance"
                        call?.disconnect()
                        return
                    }
                }
                // Check if elapsed time exceeds max time
                if (elapsedTimeInMillis >= maxTimeInMillis) {
                    stopCallTimer()
                    binding.callStatus.text = "Call ended due to time limit"
                    call?.disconnect()
                } else {
                    handler?.postDelayed(this, 1000)
                }
            }
        }

        handler?.post(runnable)
    }

    private fun stopCallTimer() {
        /*timer?.cancel()
        timer = null*/
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }
}