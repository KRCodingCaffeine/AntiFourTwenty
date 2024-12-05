package com.arista.antifourtwenty.features

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.data.network.RetrofitClient
import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.data.repository.auth.LoginRepositoryImpl
import com.arista.antifourtwenty.common.data.repository.constants.ConstantRepositoryImpl
import com.arista.antifourtwenty.common.data.repository.twilio.TokenRepositoryImpl
import com.arista.antifourtwenty.common.data.repository.wallet.WalletRepositoryImpl
import com.arista.antifourtwenty.common.domain.models.auth.LoginResponse
import com.arista.antifourtwenty.common.domain.models.getconstant.GetConstantResponse
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponse
import com.arista.antifourtwenty.common.domain.usecase.constants.GetConstantUseCaseImpl
import com.arista.antifourtwenty.common.domain.usecase.twilio.TwilioUseCase
import com.arista.antifourtwenty.common.domain.usecase.twilio.TwilioUseCaseImpl
import com.arista.antifourtwenty.common.domain.usecase.wallet.WalletUseCaseImpl
import com.arista.antifourtwenty.common.utils.CustomDialog
import com.arista.antifourtwenty.common.utils.OnBalanceUpdateListener
import com.arista.antifourtwenty.common.utils.PermissionManager
import com.arista.antifourtwenty.databinding.ActivityMainBinding
import com.arista.antifourtwenty.features.auth.LoginActivity
import com.arista.antifourtwenty.features.call.CallActivity
import com.arista.antifourtwenty.features.care.CustomerCareFragment
import com.arista.antifourtwenty.features.history.CallHistoryFragment
import com.arista.antifourtwenty.features.home.HomeFragment
import com.arista.antifourtwenty.features.upgradeapp.UpgradeAppFragment
import com.arista.antifourtwenty.features.viewmodel.MainActivityViewModel
import com.arista.antifourtwenty.features.viewmodel.MainActivityViewModelFactory
import com.arista.antifourtwenty.features.wallet.WalletFragment
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), OnBalanceUpdateListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferenceManager
    var accessToken: String = ""
    var wallet_balance = ""
    private val mainActivityViewModel: MainActivityViewModel by viewModels {
        val permissionManager = PermissionManager(this)
        val sharedPreferenceManager = SharedPreferenceManager(this)
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val twilioRepositoryImpl = TokenRepositoryImpl(apiService)
        val twilioUseCase = TwilioUseCaseImpl(twilioRepositoryImpl)
        val getConstantRepositoryImpl = ConstantRepositoryImpl(apiService)
        val getConstantUseCase = GetConstantUseCaseImpl(getConstantRepositoryImpl)
        val walletRepositoryImpl = WalletRepositoryImpl(apiService)
        val walletUseCase = WalletUseCaseImpl(walletRepositoryImpl)
        MainActivityViewModelFactory(
            permissionManager,
            sharedPreferenceManager,
            twilioUseCase,
            getConstantUseCase,
            walletUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null
        sharedPreferences = SharedPreferenceManager(this)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        wallet_balance =
            getString(R.string.rupee_symbol) + " " + sharedPreferences.getWalletBalance().toString()
        binding.walletBalance.text = wallet_balance

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // Set initial fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
      onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Show exit confirmation dialog
                showExitConfirmationDialog()
            }
        })
        setNavigationViewListener()

        if (!mainActivityViewModel.checkPermission(this)) {
            mainActivityViewModel.checkAndRequestPermissions(this)
        }

        mainActivityViewModel.getConstants()

        mainActivityViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                // Show loading indicator
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
            }
        })

        mainActivityViewModel.getConstantResponse.observe(this, Observer { response ->
            // Handle login response
            when (response) {
                is GetConstantResponse.Success -> {
                    // Handle successful login
                    val userDetails = response.message.user
                    wallet_balance =
                        getString(R.string.rupee_symbol) + " " + userDetails.walletBalance.toString()
                    binding.walletBalance.text = wallet_balance
                }

                is GetConstantResponse.Error -> {
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


    private fun setNavigationViewListener() {
        binding.customMenu.profilename.text = "Name : " + sharedPreferences.getUserName()
        binding.customMenu.welcome.text = "Mobile : " + sharedPreferences.getUserMobile().toString()
        binding.customMenu.email.text = "Email : " +sharedPreferences.getUserEmail()

        binding.customMenu.homeTextViewName.setOnClickListener {
            loadFragment(HomeFragment())
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.customMenu.keyTextViewName.setOnClickListener {
            loadFragment(UpgradeAppFragment())
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.customMenu.historyTextViewName.setOnClickListener {
            loadFragment(CallHistoryFragment())
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.customMenu.walletTextViewName.setOnClickListener {
            loadFragment(WalletFragment())
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.customMenu.customerTextViewName.setOnClickListener {
            loadFragment(CustomerCareFragment())
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.customMenu.outTextViewName.setOnClickListener {
            mainActivityViewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
           startActivity(intent)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MainActivityViewModel.REQUEST_CODE) {
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        }
    }

    fun fetchAccessToken() {

        binding.progressBar.show()
        // URL for your API endpoint
        val url = "https://twilio.developerbrothersproject.com/api/generate-token"

        // Create a Volley request queue
        val requestQueue = Volley.newRequestQueue(this)

        // Create a JSON request
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                binding.progressBar.hide()
                // Handle the JSON response
                val token = response.optString("token")
                if (token.isNotEmpty()) {
                    // Use the token as needed
                    accessToken = token
                    sharedPreferences.setTwilioToken(token)

                } else {
                    binding.progressBar.hide()
                    Log.d("strResponse", "Token not Found")
                }
            },
            { error ->
                binding.progressBar.hide()
                // Handle error
                Log.d("strResponse", "Api error!")
            }
        )

        // Add the request to the queue
        requestQueue.add(jsonObjectRequest)
    }

    override fun onBalanceUpdated(balance: String) {
        wallet_balance =
            getString(R.string.rupee_symbol) + " " + balance
        binding.walletBalance.text = wallet_balance
    }

}