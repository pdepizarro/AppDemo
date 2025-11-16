package com.pph.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponseBo(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerialName("timezone_offset") val timezoneOffset: Int,
    val current: CurrentWeatherBo? = null,
    val daily: List<DailyForecastBo> = emptyList()
)
