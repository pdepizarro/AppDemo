package com.pph.domain.repository

interface SelectedDayRepository {
    suspend fun saveSelectedDay(epochDay: Long)
    suspend fun getSelectedDay(): Long?
}