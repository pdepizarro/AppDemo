package com.pph.data.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pph.data.model.entity.SelectedDayEntity

@Dao
interface SelectedDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSelectedDay(entity: SelectedDayEntity)

    @Query("SELECT selectedDayEpoch FROM selected_day WHERE id = 0")
    suspend fun getSelectedDay(): Long?
}