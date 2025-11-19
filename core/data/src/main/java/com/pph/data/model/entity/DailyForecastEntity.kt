package com.pph.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_forecast")
data class DailyForecastEntity(
    @PrimaryKey val dateEpochDay: Long,
    val dt: Long,
    val humidity: Int,
    val windSpeed: Double,
    val dayTemp: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val nightTemp: Double,
    val weatherId: Int,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val timezoneOffset: Int,
    val lastUpdated: Long
)
