package com.pph.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDescriptionBo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)