package com.pph.data.di

import com.pph.data.repository.impl.SelectedDayRepositoryImpl
import com.pph.data.repository.impl.ForecastRepositoryImpl
import com.pph.domain.repository.ForecastRepository
import com.pph.domain.repository.SelectedDayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindings {
    @Binds
    @Singleton
    abstract fun bindForecastRepository(
        impl: ForecastRepositoryImpl
    ): ForecastRepository


    @Binds
    @Singleton
    abstract fun bindSelectedDayRepository(
        impl: SelectedDayRepositoryImpl
    ): SelectedDayRepository
}

