package com.pph.shared.ui.model

import java.time.LocalDate

data class DailyForecastUiModel(
    val date: LocalDate,
    val minTemp: Double,
    val maxTemp: Double,
    val dayTemp: Double,
    val nightTemp: Double,
    val humidity: Int,
    val windSpeed: Double,
    val main: String,
    val description: String,
    val icon: String
)