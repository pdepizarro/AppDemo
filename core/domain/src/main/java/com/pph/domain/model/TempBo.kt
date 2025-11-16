package com.pph.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TempBo(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double
)