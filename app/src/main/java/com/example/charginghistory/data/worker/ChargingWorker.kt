package com.example.charginghistory.data.worker

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.charginghistory.data.entity.ChargingHistory
import com.example.charginghistory.domain.repository.ChargingHistoryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@HiltWorker
class ChargingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ChargingHistoryRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val action = inputData.getString("action") ?: return@withContext Result.failure()
                Log.d("ChargingWorker", "Handling action: $action")
                handleAction(action)
                Log.d("ChargingWorker", "Success!")
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("ChargingWorker", "Error!")
                Result.failure()
            }
        }
    }

    /*private suspend fun handleAction(action: String) {
        val batteryStatus = applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val batteryPct = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        Log.d("ChargingWorker", "Battery percentage: $batteryPct")

        if (action == Intent.ACTION_POWER_CONNECTED) {
            val inTime = System.currentTimeMillis()
            val chargingHistory = ChargingHistory(
                inTime = inTime,
                outTime = null,
                batteryPercentageStart = batteryPct,
                batteryPercentageEnd = null
            )
            Log.d("ChargingWorker", "Inserting charging history: $chargingHistory")
            repository.insertChargingHistory(chargingHistory)
        } else if (action == Intent.ACTION_POWER_DISCONNECTED) {
            val outTime = System.currentTimeMillis()
            // Get the latest charging history
            val latestChargingHistory = runBlocking {
                repository.getAllChargingHistories().firstOrNull()?.lastOrNull()
            }

            latestChargingHistory?.let { history ->
                history.outTime = outTime
                history.batteryPercentageEnd = batteryPct
                Log.d("ChargingWorker", "Updating charging history: $history")
                repository.updateChargingHistory(history)
            }
        }
    }*/
    private suspend fun handleAction(action: String) {
        val batteryStatus = applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val batteryPct = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        Log.d("ChargingWorker", "Battery percentage: $batteryPct")

        if (action == Intent.ACTION_POWER_CONNECTED) {
            val inTime = System.currentTimeMillis()
            val chargingHistory = ChargingHistory(
                inTime = inTime,
                outTime = null,
                batteryPercentageStart = batteryPct,
                batteryPercentageEnd = null
            )
            Log.d("ChargingWorker", "Inserting charging history: $chargingHistory")
            repository.insertChargingHistory(chargingHistory)
        } else if (action == Intent.ACTION_POWER_DISCONNECTED) {
            val outTime = System.currentTimeMillis()
            val latestChargingHistory = runBlocking {
                repository.getAllChargingHistories().firstOrNull()?.lastOrNull()
            }

            latestChargingHistory?.let { history ->
                history.outTime = outTime
                history.batteryPercentageEnd = batteryPct

                // Calculate charging duration
                history.chargingDuration = outTime - history.inTime

                // Calculate battery increment
                history.batteryIncrement = batteryPct - history.batteryPercentageStart

                // Calculate overcharge duration if battery is fully charged
                if (batteryPct == 100) {
                    history.overChargeDuration = outTime - history.inTime
                }

                Log.d("ChargingWorker", "Updating charging history: $history")
                repository.updateChargingHistory(history)
            }
        }
    }
}