package com.pph.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TempDto(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double
)