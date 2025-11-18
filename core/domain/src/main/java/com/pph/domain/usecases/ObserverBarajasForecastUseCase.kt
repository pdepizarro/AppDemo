package com.pph.domain.usecases

import com.pph.domain.model.DailyForecastBo
import com.pph.domain.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBarajasForecastUseCase @Inject constructor(
    private val repository: ForecastRepository
) {
    operator fun invoke(): Flow<List<DailyForecastBo>> =
        repository.observeBarajasDailyForecast()
}