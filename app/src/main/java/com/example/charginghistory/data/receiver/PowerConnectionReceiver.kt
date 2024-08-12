package com.example.charginghistory.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast
import com.example.charginghistory.data.entity.ChargingHistory
import com.example.charginghistory.domain.repository.ChargingHistoryRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PowerConnectionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: ChargingHistoryRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val action = intent.action ?: return
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                handleAction(context, action)
            }
        }
    }

    private suspend fun handleAction(context: Context?, action: String) {
        val batteryStatus = context?.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val batteryPct = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        Log.d("PowerConnectionReceiver", "Battery percentage: $batteryPct")

        if (batteryPct <= -1) {
            Log.e("PowerConnectionReceiver", "Failed to retrieve battery percentage")
            return
        }

        if (action == Intent.ACTION_POWER_CONNECTED) {
            val inTime = System.currentTimeMillis()
            val chargingHistory = ChargingHistory(
                inTime = inTime,
                outTime = null,
                batteryPercentageStart = batteryPct,
                batteryPercentageEnd = null
            )
            Log.d("PowerConnectionReceiver", "Inserting charging history: $chargingHistory")
            repository.insertChargingHistory(chargingHistory)
        } else if (action == Intent.ACTION_POWER_DISCONNECTED) {
            val outTime = System.currentTimeMillis()
            val latestChargingHistory = withContext(Dispatchers.IO) {
                repository.getAllChargingHistories().firstOrNull()?.lastOrNull()
            }

            latestChargingHistory?.let { history ->
                // Ensure we're updating the correct record
                if (history.outTime == null) {
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

                    Log.d("PowerConnectionReceiver", "Updating charging history: $history")
                    repository.updateChargingHistory(history)
                } else {
                    Log.d("PowerConnectionReceiver", "Latest charging history is already updated, skipping.")
                }
            } ?: Log.d("PowerConnectionReceiver", "No charging history found to update")
        }
    }
}