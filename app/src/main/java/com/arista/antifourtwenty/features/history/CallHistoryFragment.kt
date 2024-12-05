package com.arista.antifourtwenty.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.common.data.network.RetrofitClient
import com.arista.antifourtwenty.common.data.network.auth.ApiService
import com.arista.antifourtwenty.common.data.repository.calllogs.CallLogsRepositoryImpl
import com.arista.antifourtwenty.common.data.repository.wallet.WalletRepositoryImpl
import com.arista.antifourtwenty.common.domain.usecase.calllogs.CallLogUseCaseImpl
import com.arista.antifourtwenty.common.domain.usecase.wallet.WalletUseCaseImpl
import com.arista.antifourtwenty.databinding.FragmentCallHistoryBinding
import com.arista.antifourtwenty.databinding.FragmentHomeBinding
import com.arista.antifourtwenty.features.history.viewmodel.CallLogViewModel
import com.arista.antifourtwenty.features.history.viewmodel.CallLogViewModelFactory
import com.arista.antifourtwenty.features.home.viewmodel.HomeViewModelFactory

class CallHistoryFragment : Fragment(R.layout.fragment_call_history) {

    private lateinit var binding : FragmentCallHistoryBinding
    private lateinit var callLogAdapter: CallLogAdapter
    private val callLogViewModel : CallLogViewModel by viewModels {
        val callLogRepositoryImpl = CallLogsRepositoryImpl(requireActivity())
        val callLogUseCase = CallLogUseCaseImpl(callLogRepositoryImpl)
        CallLogViewModelFactory(
            callLogUseCase
        )
    }

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        binding = FragmentCallHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        callLogAdapter = CallLogAdapter(emptyList())
        binding.historyRecyclerView.adapter = callLogAdapter
        callLogViewModel.callLogs.observe(requireActivity(), Observer { callLogs ->
            callLogs?.let {
                callLogAdapter.updateCallLogs(it)
            }
        })

        callLogViewModel.fetchCallLogs()
    }
}