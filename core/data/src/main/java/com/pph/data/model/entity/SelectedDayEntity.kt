package com.pph.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_day")
data class SelectedDayEntity(
    @PrimaryKey val id: Int = 0,
    val selectedDayEpoch: Long
)