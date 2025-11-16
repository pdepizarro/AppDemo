package com.pph.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDto(
    val dt: Long,
    val temp: Double,
    val humidity: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    val weather: List<WeatherDescriptionDto> = emptyList()
)
