package com.example.charginghistory.di

import com.example.charginghistory.data.repository.ChargingHistoryRepositoryImpl
import com.example.charginghistory.domain.repository.ChargingHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindChargingHistoryRepository(
        impl: ChargingHistoryRepositoryImpl
    ): ChargingHistoryRepository
}
