package com.arista.antifourtwenty.features.home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.common.data.local.SharedPreferenceManager
import com.arista.antifourtwenty.common.data.network.RetrofitClient
import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.data.repository.calllogs.CallLogsRepositoryImpl
import com.arista.antifourtwenty.common.data.repository.twilio.TokenRepositoryImpl
import com.arista.antifourtwenty.common.data.repository.wallet.WalletRepositoryImpl
import com.arista.antifourtwenty.common.domain.models.wallet.WalletResponse
import com.arista.antifourtwenty.common.domain.usecase.calllogs.CallLogUseCaseImpl
import com.arista.antifourtwenty.common.domain.usecase.twilio.TwilioUseCaseImpl
import com.arista.antifourtwenty.common.domain.usecase.wallet.WalletUseCaseImpl
import com.arista.antifourtwenty.common.utils.CustomDialog
import com.arista.antifourtwenty.common.utils.OnBalanceUpdateListener
import com.arista.antifourtwenty.databinding.FragmentHomeBinding
import com.arista.antifourtwenty.features.call.CallActivity
import com.arista.antifourtwenty.features.home.adapter.GridAdapter
import com.arista.antifourtwenty.features.home.viewmodel.HomeViewModel
import com.arista.antifourtwenty.features.home.viewmodel.HomeViewModelFactory
import kotlin.system.exitProcess

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    val numberPad = arrayOf<String>("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")
    private lateinit var contactPickerLauncher: ActivityResultLauncher<Intent>

    private var listener: OnBalanceUpdateListener? = null

    private val homeViewModel: HomeViewModel by viewModels {
        val sharedPreferenceManager = SharedPreferenceManager(requireActivity())
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val walletRepositoryImpl = WalletRepositoryImpl(apiService)

        val walletUseCase = WalletUseCaseImpl(walletRepositoryImpl)
        val callLogRepositoryImpl = CallLogsRepositoryImpl(requireActivity())
        val callLogUseCase = CallLogUseCaseImpl(callLogRepositoryImpl)
        val twilioRepositoryImpl = TokenRepositoryImpl(apiService)
        val twilioUseCase = TwilioUseCaseImpl(twilioRepositoryImpl)
        HomeViewModelFactory(
            walletUseCase, sharedPreferenceManager, callLogUseCase
        )
    }

    companion object {
        const val CALL_REQUEST_CODE = 1
    }

    private lateinit var callActivityResultLauncher: ActivityResultLauncher<Intent>
    private var mobileNumber: String = ""

    private var twilioToken: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBalanceUpdateListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnBalanceUpdateListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferenceManager = context?.let { SharedPreferenceManager(it) }!!

        binding.grid.adapter = context?.let { GridAdapter(it, numberPad) }

        contactPickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        processContactData(uri)
                    }
                }
            }

        callActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val finalBalance = result.data?.getStringExtra("final_balance")
                // Handle the final balance here
                if (finalBalance != null) {
                    homeViewModel.updateWallet(finalBalance)
                }
            }
        }
        homeViewModel.loading.observe(requireActivity(), Observer { isLoading ->
            if (isLoading) {
                // Show loading indicator
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
            }
        })

        homeViewModel.walletResponse.observe(requireActivity(), Observer { walletResponse ->
            // Handle login response
            when (walletResponse) {
                is WalletResponse.Success -> {
                    // Handle successful login
                    val walletDetails = walletResponse.message
                    listener?.onBalanceUpdated(walletDetails.wallet_balance.toString())
                }

                is WalletResponse.Error -> {

                }
            }
        })
        gridClickEvent()
        fetchAccessToken()
        binding.contactButton.setOnClickListener {
            pickContact()
        }
        binding.delButton.setOnClickListener {
            deleteCharacterBeforeCursor()
        }
        binding.callButton.setOnClickListener {
            mobileNumber = binding.mobileNumber.text.toString()
            if (!validateMobileValue(mobileNumber)) {
                if (sharedPreferenceManager.getTwilioToken()?.isNotEmpty() == true) {
                    if (sharedPreferenceManager.getWalletBalance()!! < 3 && sharedPreferenceManager.getAppCategory() == "base") {
                        val customDialog = CustomDialog(requireActivity())
                        customDialog.setTitle("Upgrade Anti 420")
                        customDialog.setMessage("The free plan has expired. Please upgrade to paid talktime by mailing at support@anti420.top")
                        customDialog.setButtonText("OK")
                        customDialog.setOnButtonClickListener(View.OnClickListener {
                            // Perform any action you want on button click
                            customDialog.dismiss()
                        })
                        customDialog.show()
                    } else if (sharedPreferenceManager.getWalletBalance()!! < 3 && sharedPreferenceManager.getAppCategory() == "upgrade") {
                        val customDialog = CustomDialog(requireActivity())
                        customDialog.setTitle("Recharge Anti 420")
                        customDialog.setMessage("Please recharge your Anti 420 wallet account for making calls.")
                        customDialog.setButtonText("OK")
                        customDialog.setOnButtonClickListener(View.OnClickListener {
                            // Perform any action you want on button click
                            customDialog.dismiss()
                        })
                        customDialog.show()
                    } else if (sharedPreferenceManager.getWalletBalance()!! >= 3) {
                        makeCall()
                    }
                } else {
                    fetchAccessToken()
                }
            } else {
                val customDialog = CustomDialog(requireActivity())
                customDialog.setTitle("Anti 420")
                customDialog.setMessage("Please dial valid mobile number.")
                customDialog.setButtonText("OK")
                customDialog.setOnButtonClickListener(View.OnClickListener {
                    // Perform any action you want on button click
                    customDialog.dismiss()
                })
                customDialog.show()
            }
        }
        homeViewModel.loading.observe(requireActivity(), Observer { isLoading ->
            if (isLoading) {
                // Show loading indicator
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
            }
        })


    }

    fun fetchAccessToken() {

        binding.progressBar.show()
        // URL for your API endpoint
        val url = "https://krcodingcaffeine.com/anti/api/generate-token"

        // Create a Volley request queue
        val requestQueue = Volley.newRequestQueue(context)

        // Create a JSON request
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                binding.progressBar.hide()
                // Handle the JSON response
                val token = response.optString("token")
                if (token.isNotEmpty()) {
                    // Use the token as needed
                    // accessToken = token
                    sharedPreferenceManager.setTwilioToken(token)
                    //makeCall()

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

    private fun validateMobileValue(mobileNumber: String?): Boolean {
        var result = false
        when {
            mobileNumber.isNullOrEmpty() -> {
                result = true
            }

            mobileNumber.length < 10 -> {
                result = true
            }
        }
        return result
    }

    private fun makeCall() {
        homeViewModel.addCallLogs(mobileNumber)
        mobileNumber = "91" + binding.mobileNumber.text
        val intent = Intent(context, CallActivity::class.java)
        intent.putExtra("mobile", mobileNumber)
        callActivityResultLauncher.launch(intent)
    }


    private fun appendMobileNo(number: String) {
        binding.mobileNumber.append(number)
    }

    private fun gridClickEvent() {
        binding.grid.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> appendMobileNo("1")
                1 -> appendMobileNo("2")
                2 -> appendMobileNo("3")
                3 -> appendMobileNo("4")
                4 -> appendMobileNo("5")
                5 -> appendMobileNo("6")
                6 -> appendMobileNo("7")
                7 -> appendMobileNo("8")
                8 -> appendMobileNo("9")
                9 -> appendMobileNo("*")
                10 -> appendMobileNo("0")
                11 -> appendMobileNo("#")
            }
        }
    }

    private fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        contactPickerLauncher.launch(intent)
    }

    @SuppressLint("Range")
    private fun processContactData(uri: Uri) {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use { c ->
            if (c.moveToFirst()) {
                var phoneNumber = ""
                var emailAddress = ""
                val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                val hasPhone =
                    c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhone.toBoolean()) {
                    val phones = requireActivity().contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                        arrayOf(contactId),
                        null
                    )
                    phones?.use { p ->
                        while (p.moveToNext()) {
                            phoneNumber =
                                p.getString(p.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
                    }
                }

                // Find Email Addresses
                val emails = requireActivity().contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
                    arrayOf(contactId),
                    null
                )
                emails?.use { e ->
                    while (e.moveToNext()) {
                        emailAddress =
                            e.getString(e.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    }
                }

                val mobile = when {
                    phoneNumber.startsWith("0") -> phoneNumber.substring(1)
                        .replace("\\s+".toRegex(), "")

                    phoneNumber.startsWith("+91") -> phoneNumber.substring(3)
                        .replace("\\s+".toRegex(), "")

                    phoneNumber.isNotBlank() -> phoneNumber.replace("\\s+".toRegex(), "")
                    else -> ""
                }

                if (mobile.length == 10) {
                    // Update your UI or data here
                    binding.mobileNumber.setText(mobile)
                }

                Log.d("curs", "$name num: $phoneNumber mail: $emailAddress")
            }
        }
    }

    private fun deleteCharacterBeforeCursor() {
        val cursorPosition = binding.mobileNumber.selectionStart

        if (cursorPosition > 0) {
            binding.mobileNumber.text?.let {
                it.delete(cursorPosition - 1, cursorPosition)
                binding.mobileNumber.setSelection(cursorPosition - 1)
            }
        } else if (binding.mobileNumber.text.toString().length >= 1) {
            val cursorPos = binding.mobileNumber.text.toString().length
            binding.mobileNumber.text?.let {
                it.delete(cursorPos - 1, cursorPos)
                binding.mobileNumber.setSelection(cursorPos - 1)
            }
        }
    }

}