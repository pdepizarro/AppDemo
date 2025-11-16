package com.pph.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyForecastBo(
    val dt: Long,
    val humidity: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    val temp: TempBo,
    val weather: List<WeatherDescriptionBo> = emptyList()
)
