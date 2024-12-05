package com.arista.antifourtwenty.features.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.data.network.RetrofitClient
import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.data.repository.auth.LoginRepositoryImpl
import com.arista.antifourtwenty.common.data.repository.auth.RegisterRepositoryImpl
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.auth.register.RegisterResponse
import com.arista.antifourtwenty.common.domain.usecase.auth.LoginUseCaseImpl
import com.arista.antifourtwenty.common.domain.usecase.auth.register.RegisterUseCaseImpl
import com.arista.antifourtwenty.common.utils.CustomDialog
import com.arista.antifourtwenty.databinding.ActivityLoginBinding
import com.arista.antifourtwenty.databinding.ActivityRegisterBinding
import com.arista.antifourtwenty.features.MainActivity
import com.arista.antifourtwenty.features.auth.viewmodel.LoginViewModel
import com.arista.antifourtwenty.features.auth.viewmodel.LoginViewModelFactory
import kotlin.system.exitProcess

class RegisterActivity : AppCompatActivity(){
    private lateinit var binding: ActivityRegisterBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.bottomlay.setOnClickListener {
            Log.d("antiapp", "register button clicked")
            val name = binding.page1.firstname.text.toString()
            val mobile = binding.page1.regMobile.text.toString()
            val email = binding.page1.email.text.toString()
            if(!validateRegisterValue(name, email, mobile)){
                loginViewModel.register(name, mobile,email)
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

        loginViewModel.registerResponse.observe(this, Observer { registerResponse ->
            // Handle login response
            when (registerResponse) {
                is RegisterResponse.Success -> {
                    // Handle successful login
                    val userDetails = registerResponse.message
                    Log.d("anti", "Logged in")
                    startActivity(Intent(this, MainActivity::class.java))
                }

                is RegisterResponse.Error -> {
                    // Handle login error
                    val customDialog = CustomDialog(this)
                    customDialog.setTitle("Register")
                    customDialog.setMessage("Something went wrong.")
                    customDialog.setButtonText("OK")
                    customDialog.setOnButtonClickListener(View.OnClickListener {
                        // Perform any action you want on button click
                        customDialog.dismiss()
                    })
                    customDialog.show()
                }
            }
        })

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

    private fun validateRegisterValue(name: String?, mobile: String?, email : String?): Boolean {
        var result = false
        when {
            name.isNullOrEmpty() && mobile.isNullOrEmpty() && email.isNullOrEmpty()-> {
                binding.page1.firstname.error = "Enter Username"
                result = true
            }
            name.isNullOrEmpty() -> {
                binding.page1.firstname.error = "Enter Username"
                result = true
            }
            mobile.isNullOrEmpty() -> {
                binding.page1.regMobile.error = "Enter Mobile"
                result = true
            }
            email.isNullOrEmpty() -> {
                binding.page1.email.error = "Enter Email"
                result = true
            }
        }
        return result
    }
}