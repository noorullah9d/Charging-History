package com.example.charginghistory.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.charginghistory.data.entity.ChargingHistory

@Database(entities = [ChargingHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chargingHistoryDao(): ChargingHistoryDao
}
