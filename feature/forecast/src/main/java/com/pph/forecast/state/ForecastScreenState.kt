package com.pph.forecast.state

import com.pph.shared.ui.model.DailyForecastUiModel

data class ForecastScreenState(
    val isLoading: Boolean = false,
    val forecast: List<DailyForecastUiModel> = emptyList(),
    val errorMessage: String? = null,
    val lastSelectedEpochDay: Long? = null
)