package com.pph.forecast.state

import com.pph.shared.ui.model.DailyForecastUiModel

data class ForecastScreenState(
    val forecast: List<DailyForecastUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {

}