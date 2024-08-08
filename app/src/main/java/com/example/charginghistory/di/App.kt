package com.example.charginghistory.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.charginghistory.data.worker.ChargingWorker
import com.example.charginghistory.domain.repository.ChargingHistoryRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: CustomWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}

class CustomWorkerFactory @Inject constructor(
    private val repository: ChargingHistoryRepository
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = ChargingWorker(appContext, workerParameters, repository)
}