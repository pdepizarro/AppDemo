package com.pph.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DailyForecastBo(
    val dateEpochDay: Long,
    val dt: Long,
    val humidity: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    val temp: TempBo,
    val weather: List<WeatherDescriptionBo> = emptyList()
) {
    val localDate: LocalDate get() = LocalDate.ofEpochDay(dateEpochDay)
}
