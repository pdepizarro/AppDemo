package com.pph.data.repository.impl

import com.pph.data.mapper.toBoList
import com.pph.data.mapper.toDailyForecastEntities
import com.pph.data.model.dao.ForecastDao
import com.pph.data.remote.api.ForecastApi
import com.pph.data.remote.safeCall
import com.pph.domain.model.DailyForecastBo
import com.pph.domain.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class ForecastRepositoryImpl @Inject constructor(
    private val api: ForecastApi,
    private val barajasForecastDao: ForecastDao,
    @param:Named("openWeatherApiKey") private val apiKey: String
) : ForecastRepository {

    //OBSERVE DB: Returns a Flow of BD data
    override fun observeBarajasDailyForecast(): Flow<List<DailyForecastBo>> =
        barajasForecastDao
            .getDailyForecast()
            .map { entities -> entities.toBoList() }

    // REFRESH: Calls the API and updates the Room database
    override suspend fun refreshBarajasForecast(): Result<Unit> =
        safeCall {
            val dto = api.getForecast(apiKey = apiKey)

            val now = System.currentTimeMillis()
            val entities = dto.toDailyForecastEntities(now)

            barajasForecastDao.clearDailyForecast()
            barajasForecastDao.insertDailyForecast(entities)
        }
}