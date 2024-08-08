package com.example.charginghistory.di

import android.content.Context
import androidx.room.Room
import com.example.charginghistory.data.db.AppDatabase
import com.example.charginghistory.data.db.ChargingHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "charging_database"
        ).build()
    }

    @Provides
    fun provideChargingHistoryDao(database: AppDatabase): ChargingHistoryDao {
        return database.chargingHistoryDao()
    }
}