package com.pph.data.repository.impl

import com.pph.data.model.dao.SelectedDayDao
import com.pph.data.model.entity.SelectedDayEntity
import com.pph.domain.repository.SelectedDayRepository
import javax.inject.Inject

class SelectedDayRepositoryImpl @Inject constructor(
    private val dao: SelectedDayDao
) : SelectedDayRepository {

    override suspend fun saveSelectedDay(epochDay: Long) {
        dao.saveSelectedDay(
            SelectedDayEntity(
                id = 0,
                selectedDayEpoch = epochDay
            )
        )
    }

    override suspend fun getSelectedDay(): Long? = dao.getSelectedDay()
}