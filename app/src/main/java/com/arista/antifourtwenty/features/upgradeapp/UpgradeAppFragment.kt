package com.arista.antifourtwenty.features.upgradeapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.data.network.RetrofitClient
import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.data.repository.auth.LoginRepositoryImpl
import com.arista.antifourtwenty.common.data.repository.upgradeapp.UpgradeAppRepositoryImpl
import com.arista.antifourtwenty.common.domain.models.upgradeapp.UpgradeAppResponse
import com.arista.antifourtwenty.common.domain.usecase.auth.LoginUseCaseImpl
import com.arista.antifourtwenty.common.domain.usecase.upgradeapp.UpgradeAppUseCaseImpl
import com.arista.antifourtwenty.common.utils.CustomDialog
import com.arista.antifourtwenty.databinding.FragmentHomeBinding
import com.arista.antifourtwenty.databinding.FragmentUpgradeAppBinding
import com.arista.antifourtwenty.features.upgradeapp.viewmodel.UpgradeAppViewModel
import com.arista.antifourtwenty.features.upgradeapp.viewmodel.UpgradeAppViewModelFactory

class UpgradeAppFragment : Fragment(R.layout.fragment_upgrade_app) {

    private lateinit var binding : FragmentUpgradeAppBinding

    private val upgradeAppViewModel:UpgradeAppViewModel by viewModels {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val upgradeAppRepository = UpgradeAppRepositoryImpl(apiService)
        val upgradeAppUseCase = UpgradeAppUseCaseImpl(upgradeAppRepository)
        val sharedPreferenceManager = SharedPreferenceManager(requireActivity())
        UpgradeAppViewModelFactory(upgradeAppUseCase, sharedPreferenceManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        binding = FragmentUpgradeAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upgradeAppViewModel.loading.observe(requireActivity(), Observer { isLoading ->
            if (isLoading) {
                // Show loading indicator
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
            }
        })

        upgradeAppViewModel.upgradeAppResponse.observe(requireActivity(), Observer { upgradeAppResponse ->
            when(upgradeAppResponse){

                is UpgradeAppResponse.Error -> {
                    val customDialog = CustomDialog(requireActivity())
                    customDialog.setTitle("Upgrade App")
                    customDialog.setMessage("Something went wrong. Please contact admin.")
                    customDialog.setButtonText("OK")
                    customDialog.setOnButtonClickListener(View.OnClickListener {
                        // Perform any action you want on button click
                        customDialog.dismiss()
                    })
                    customDialog.show()
                }

                is UpgradeAppResponse.Success -> {
                    Log.d("anti", "Logged in")
                    val customDialog = CustomDialog(requireActivity())
                    customDialog.setTitle("Upgrade App")
                    customDialog.setMessage("App upgraded successfully.")
                    customDialog.setButtonText("OK")
                    customDialog.setOnButtonClickListener(View.OnClickListener {
                        // Perform any action you want on button click
                        customDialog.dismiss()
                    })
                    customDialog.show()
                }
            }
        })
        binding.addMoneyButton.setOnClickListener {
            upgradeAppViewModel.upgradeApp("66R365TN4OZB7T8C")
        }
    }
}