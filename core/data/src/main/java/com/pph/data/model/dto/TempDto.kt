package com.pph.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class TempDto(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double
)