package com.pph.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherBo(
    val dt: Long,
    val temp: Double,
    val humidity: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    val weather: List<WeatherDescriptionBo> = emptyList()
)
