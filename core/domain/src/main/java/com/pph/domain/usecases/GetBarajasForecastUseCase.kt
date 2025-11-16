package com.pph.domain.usecases

import com.pph.domain.model.ForecastResponseBo
import com.pph.domain.repository.ForecastRepository
import javax.inject.Inject

class GetBarajasForecastUseCase @Inject constructor(
    private val repository: ForecastRepository
) {
    suspend operator fun invoke(): Result<ForecastResponseBo> =
        repository.getBarajasForecast()
}
