package com.pph.shared.ui.model

import java.time.LocalDate

data class DailyForecastUiModel(
    val date: LocalDate,
    val minTemp: Int,
    val maxTemp: Int,
    val humidity: Int,
    val windSpeed: Int,
    val condition: String
)