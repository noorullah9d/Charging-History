package com.example.charginghistory.domain.repository

import com.example.charginghistory.data.entity.ChargingHistory
import kotlinx.coroutines.flow.Flow

interface ChargingHistoryRepository {
    suspend fun insertChargingHistory(chargingHistory: ChargingHistory)
    suspend fun updateChargingHistory(chargingHistory: ChargingHistory)
    suspend fun deleteChargingHistory(chargingHistory: ChargingHistory)
    fun getAllChargingHistories(): Flow<List<ChargingHistory>>
}
