package com.pph.domain.repository

import com.pph.domain.model.DailyForecastBo
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    /**
     * Reads local cached forecast from Room.
     */
    fun observeBarajasDailyForecast(): Flow<List<DailyForecastBo>>
    /**
     * Refreshes data from OpenWeather API and stores it into Room.
     */
    suspend fun refreshBarajasForecast(): Result<Unit>
}