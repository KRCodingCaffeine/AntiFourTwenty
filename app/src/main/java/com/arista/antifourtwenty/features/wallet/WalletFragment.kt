package com.arista.antifourtwenty.features.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.databinding.FragmentHomeBinding
import com.arista.antifourtwenty.databinding.FragmentWalletBinding

class WalletFragment : Fragment(R.layout.fragment_wallet) {

    private lateinit var binding : FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example: Set a click listener for the button
        //binding.textView.text = "Wallet Screen"
    }
}