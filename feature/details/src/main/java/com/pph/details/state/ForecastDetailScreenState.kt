package com.pph.details.state

import com.pph.shared.ui.model.DailyForecastUiModel

data class ForecastDetailScreenState(
    val isLoading: Boolean = false,
    val detail: DailyForecastUiModel? = null,
    val errorMessage: String? = null,
)