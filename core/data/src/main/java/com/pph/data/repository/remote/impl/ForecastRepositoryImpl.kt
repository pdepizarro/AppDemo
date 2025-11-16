package com.pph.data.repository.remote.impl

import com.pph.data.mapper.toBo
import com.pph.data.repository.remote.api.ForecastApi
import com.pph.data.repository.remote.safeCall
import com.pph.domain.model.ForecastResponseBo
import com.pph.domain.repository.ForecastRepository
import javax.inject.Inject
import javax.inject.Named

class ForecastRepositoryImpl @Inject constructor(
    private val api: ForecastApi,
    @param:Named("openWeatherApiKey") private val apiKey: String
) : ForecastRepository {

    override suspend fun getBarajasForecast(): Result<ForecastResponseBo> =
        safeCall {
            api.getForecast(apiKey = apiKey).toBo()
        }
}