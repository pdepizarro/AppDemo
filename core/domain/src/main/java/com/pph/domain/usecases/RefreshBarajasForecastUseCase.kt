package com.pph.domain.usecases

import com.pph.domain.repository.ForecastRepository
import javax.inject.Inject

class RefreshBarajasForecastUseCase @Inject constructor(
    private val repository: ForecastRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        repository.refreshBarajasForecast()
}
