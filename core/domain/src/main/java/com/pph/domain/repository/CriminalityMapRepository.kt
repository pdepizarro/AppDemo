package com.pph.domain.repository

import com.pph.domain.model.CriminalityMapData

interface CriminalityMapRepository {
    suspend fun getAllData(): Result<CriminalityMapData>
}