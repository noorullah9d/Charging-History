package com.example.charginghistory.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "charging_history")
data class ChargingHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val inTime: Long,
    var outTime: Long?,
    val batteryPercentageStart: Int,
    var batteryPercentageEnd: Int?
)
