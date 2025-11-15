package com.pph.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CriminalityMapData(
    val x: Int,
    val y: Int,
)