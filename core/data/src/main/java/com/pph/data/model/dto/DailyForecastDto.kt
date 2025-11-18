package com.pph.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyForecastDto(
    val dt: Long,
    val humidity: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    val temp: TempDto,
    val weather: List<WeatherDescriptionDto> = emptyList()
)
