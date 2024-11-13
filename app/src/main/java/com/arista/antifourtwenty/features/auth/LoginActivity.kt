package com.arista.antifourtwenty.features.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.features.auth.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_login)


        // Observe login response
        loginViewModel.loginResponse.observe(this, Observer { loginResponse ->
            // Handle login response
            when (loginResponse) {
                is LoginResponse.Success -> {
                    // Handle successful login
                    val userDetails = loginResponse.message
                    Log.d("anti", "Logged in")
                }

                is LoginResponse.Error -> {
                    // Handle login error
                    Log.d("anti", "Error")
                }
            }
        })

        // Observe error message
        loginViewModel.errorMessage.observe(this, Observer { errorMessage ->
            // TODO: Show error message
            Log.d("anti", errorMessage)
        })

        // Example login attempt
        loginViewModel.login("9876543210", "EGPXWZEQGZVC6JTX")
    }
}