package com.example.charginghistory.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.charginghistory.data.entity.ChargingHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ChargingHistoryDao {
    @Insert
    suspend fun insert(history: ChargingHistory)

    @Update
    suspend fun update(history: ChargingHistory)

    @Delete
    suspend fun delete(history: ChargingHistory)

    @Query("SELECT * FROM charging_history")
    fun getAll(): Flow<List<ChargingHistory>>
}
