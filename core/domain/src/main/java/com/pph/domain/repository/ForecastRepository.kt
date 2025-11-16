package com.pph.domain.repository

import com.pph.domain.model.ForecastResponseBo

interface ForecastRepository {
    suspend fun getBarajasForecast(): Result<ForecastResponseBo>
}