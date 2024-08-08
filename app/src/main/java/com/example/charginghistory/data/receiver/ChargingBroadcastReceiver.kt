package com.example.charginghistory.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.charginghistory.data.worker.ChargingWorker

class ChargingBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val action = intent.action ?: return
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show()
            Log.d("ChargingReceiver", "Received intent: $action")
            val data = Data.Builder().putString("action", action).build()
            val workRequest = OneTimeWorkRequestBuilder<ChargingWorker>()
                .setInputData(data)
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
            Log.d("ChargingReceiver", "WorkManager enqueued for action: $action")
        }
    }
}