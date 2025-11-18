package com.pph.data.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pph.data.model.entity.DailyForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Query("SELECT * FROM daily_forecast ORDER BY dateEpochDay")
    fun getDailyForecast(): Flow<List<DailyForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(items: List<DailyForecastEntity>)

    @Query("DELETE FROM daily_forecast")
    suspend fun clearDailyForecast()
}