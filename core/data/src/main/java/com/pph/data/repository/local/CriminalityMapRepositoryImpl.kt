package com.pph.data.repository.local

import com.pph.data.repository.remote.safeCall
import com.pph.domain.model.CriminalityMapData
import com.pph.domain.repository.CriminalityMapRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CriminalityMapRepositoryImpl @Inject constructor() : CriminalityMapRepository {

    // Here we could implement Room if we would like to persist data
    private var setup: CriminalityMapData? = null

    override suspend fun getAllData(): Result<CriminalityMapData> = safeCall {
        delay(1000)
        setup?.let { setup } ?: run { throw Exception(" No setup") }
    }
}