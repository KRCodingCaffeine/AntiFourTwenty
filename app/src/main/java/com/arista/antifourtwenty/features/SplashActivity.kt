package com.arista.antifourtwenty.features

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.features.auth.LoginActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        sharedPreferenceManager = SharedPreferenceManager(this)
        //val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
       // val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Navigate to the appropriate activity
        if (sharedPreferenceManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}