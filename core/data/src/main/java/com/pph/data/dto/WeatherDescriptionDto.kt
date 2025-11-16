package com.pph.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDescriptionDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)