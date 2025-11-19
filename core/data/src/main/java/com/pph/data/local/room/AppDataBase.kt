package com.pph.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pph.data.model.dao.ForecastDao
import com.pph.data.model.dao.SelectedDayDao
import com.pph.data.model.entity.DailyForecastEntity
import com.pph.data.model.entity.SelectedDayEntity

@Database(
    entities = [SelectedDayEntity::class, DailyForecastEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selectedDayDao(): SelectedDayDao
    abstract fun barajasForecastDao(): ForecastDao
}
