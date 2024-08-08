package com.example.charginghistory.presentation.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.charginghistory.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding

    private val viewModel: ChargingHistoryViewModel by viewModels()
    private lateinit var adapter: ChargingHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChargingHistoryAdapter(
            onDeleteClick = {
                viewModel.deleteChargingHistory(it)
            }
        )
        binding.recyclerView.adapter = adapter

        // Collect the StateFlow in the fragment's lifecycleScope
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chargingHistories.collect { histories ->
                adapter.submitList(histories)
            }
        }
    }
}