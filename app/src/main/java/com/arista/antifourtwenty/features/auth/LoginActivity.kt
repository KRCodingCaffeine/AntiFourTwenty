package com.arista.antifourtwenty.features.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.features.auth.viewmodel.LoginViewModel
import com.arista.antifourtwenty.common.domain.usecase.auth.LoginUseCase
import com.arista.antifourtwenty.features.auth.viewmodel.LoginViewModelFactory
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.data.network.RetrofitClient
import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.data.repository.auth.LoginRepositoryImpl
import com.arista.antifourtwenty.common.data.repository.auth.RegisterRepositoryImpl
import com.arista.antifourtwenty.common.domain.repository.auth.LoginRepository
import com.arista.antifourtwenty.common.domain.usecase.auth.LoginUseCaseImpl
import com.arista.antifourtwenty.common.domain.usecase.auth.register.RegisterUseCaseImpl
import com.arista.antifourtwenty.common.utils.CustomDialog
import com.arista.antifourtwenty.databinding.ActivityLoginBinding
import com.arista.antifourtwenty.features.MainActivity
import com.arista.antifourtwenty.features.auth.register.RegisterActivity
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels {
        // Manually create the required objects and pass them to the ViewModelFactory
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val loginRepository = LoginRepositoryImpl(apiService)
        val loginUseCase = LoginUseCaseImpl(loginRepository)
        val registerRepository = RegisterRepositoryImpl(apiService)
        val registerUseCase = RegisterUseCaseImpl(registerRepository)
        val sharedPreferenceManager = SharedPreferenceManager(this)
        LoginViewModelFactory(loginUseCase, registerUseCase, sharedPreferenceManager)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        supportActionBar?.hide()

        loginViewModel.loginResponse.observe(this, Observer { loginResponse ->
            // Handle login response
            when (loginResponse) {
                is LoginResponse.Success -> {
                    // Handle successful login
                    val userDetails = loginResponse.message
                    Log.d("anti", "Logged in")
                    startActivity(Intent(this, MainActivity::class.java))
                }

                is LoginResponse.Error -> {
                    // Handle login error
                    val customDialog = CustomDialog(this)
                    customDialog.setTitle("Login")
                    customDialog.setMessage("No users were found.")
                    customDialog.setButtonText("OK")
                    customDialog.setOnButtonClickListener(View.OnClickListener {
                    // Perform any action you want on button click
                    customDialog.dismiss()
                    })
                    customDialog.show()
                }
            }
        })

        binding.logincircularButton1.setOnClickListener {
            val firstName = binding.firstname.text.toString()
            val userKey = binding.password.text.toString()
            if(!validateLoginValue(firstName, userKey)){
                loginViewModel.login(firstName, userKey)
            }
        }

        loginViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                // Show loading indicator
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
            }
        })

        binding.newacnt.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Custom back press logic here
                // For example, showing a confirmation dialog
                showExitConfirmationDialog()
            }
        })

        binding.backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun showExitConfirmationDialog() {
        val customDialog = CustomDialog(this)
        customDialog.setTitle("Anti 420")
        customDialog.setMessage("Are you sure, to close the application.")
        customDialog.setButtonText("OK")
        customDialog.setOnButtonClickListener(View.OnClickListener {
            // Perform any action you want on button click
            customDialog.dismiss()
            finishAffinity()
            exitProcess(0)
        })
        customDialog.show()
    }

    private fun validateLoginValue(name: String?, pwd: String?): Boolean {
        var result = false
        when {
            name.isNullOrEmpty() && pwd.isNullOrEmpty() -> {
                binding.firstname.error = "Enter Username"
                result = true
            }
            name.isNullOrEmpty() -> {
                binding.firstname.error = "Enter Username"
                result = true
            }
            pwd.isNullOrEmpty() -> {
                binding.password.error = "Enter User Key"
                result = true
            }
        }
        return result
    }
}