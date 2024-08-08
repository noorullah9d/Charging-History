package com.example.charginghistory.presentation.fragments.history

import androidx.lifecycle.*
import com.example.charginghistory.data.entity.ChargingHistory
import com.example.charginghistory.domain.repository.ChargingHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChargingHistoryViewModel @Inject constructor(
    private val repository: ChargingHistoryRepository
) : ViewModel() {

    private val _chargingHistories = MutableStateFlow<List<ChargingHistory>>(emptyList())
    val chargingHistories: StateFlow<List<ChargingHistory>> get() = _chargingHistories

    init {
        fetchChargingHistories()
    }

    private fun fetchChargingHistories() {
        viewModelScope.launch {
            repository.getAllChargingHistories()
                .collect { histories ->
                    _chargingHistories.value = histories.reversed() // Reverse to show latest first
                }
        }
    }

    fun insertChargingHistory(chargingHistory: ChargingHistory) {
        viewModelScope.launch {
            repository.insertChargingHistory(chargingHistory)
            fetchChargingHistories() // Refresh the list after insertion
        }
    }

    fun updateChargingHistory(chargingHistory: ChargingHistory) {
        viewModelScope.launch {
            repository.updateChargingHistory(chargingHistory)
            fetchChargingHistories() // Refresh the list after update
        }
    }

    fun deleteChargingHistory(chargingHistory: ChargingHistory) {
        viewModelScope.launch {
            repository.deleteChargingHistory(chargingHistory)
        }
    }
}

