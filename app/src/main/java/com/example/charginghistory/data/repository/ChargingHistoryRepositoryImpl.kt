package com.example.charginghistory.data.repository

import com.example.charginghistory.data.db.ChargingHistoryDao
import com.example.charginghistory.data.entity.ChargingHistory
import com.example.charginghistory.domain.repository.ChargingHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChargingHistoryRepositoryImpl @Inject constructor(
    private val dao: ChargingHistoryDao
) : ChargingHistoryRepository {
    override suspend fun insertChargingHistory(chargingHistory: ChargingHistory) {
        dao.insert(chargingHistory)
    }

    override suspend fun updateChargingHistory(chargingHistory: ChargingHistory) {
        dao.update(chargingHistory)
    }

    override suspend fun deleteChargingHistory(chargingHistory: ChargingHistory) {
        dao.delete(chargingHistory)
    }

    override fun getAllChargingHistories(): Flow<List<ChargingHistory>> {
        return dao.getAll()
    }
}
