package com.pph.data.mapper

import com.pph.data.model.dto.DailyForecastDto
import com.pph.data.model.dto.ForecastResponseDto
import com.pph.data.model.entity.DailyForecastEntity
import com.pph.domain.model.DailyForecastBo
import com.pph.domain.model.TempBo
import com.pph.domain.model.WeatherDescriptionBo
import java.time.Instant
import java.time.ZoneOffset

// ---------------------------------------------------------------------------
//  DTO -> ENTITY
// ---------------------------------------------------------------------------

fun DailyForecastDto.toEntity(
    timezoneOffset: Int,
    now: Long
): DailyForecastEntity =
    DailyForecastEntity(
        dateEpochDay = dt.toEpochDayWithOffset(timezoneOffset),
        dt = dt,
        humidity = humidity,
        windSpeed = windSpeed,

        dayTemp = temp.day,
        minTemp = temp.min,
        maxTemp = temp.max,
        nightTemp = temp.night,

        weatherId = weather.firstOrNull()?.id ?: -1,
        weatherMain = weather.firstOrNull()?.main.orEmpty(),
        weatherDescription = weather.firstOrNull()?.description.orEmpty(),
        weatherIcon = weather.firstOrNull()?.icon.orEmpty(),

        timezoneOffset = timezoneOffset,
        lastUpdated = now
    )

fun ForecastResponseDto.toDailyForecastEntities(
    now: Long
): List<DailyForecastEntity> =
    daily.map { it.toEntity(timezoneOffset, now) }

private fun Long.toEpochDayWithOffset(offsetSeconds: Int): Long {
    val offset = ZoneOffset.ofTotalSeconds(offsetSeconds)
    return Instant.ofEpochSecond(this)
        .atOffset(offset)
        .toLocalDate()
        .toEpochDay()
}

// ---------------------------------------------------------------------------
//  ENTITY -> BO
// ---------------------------------------------------------------------------

fun DailyForecastEntity.toBo(): DailyForecastBo =
    DailyForecastBo(
        dateEpochDay = dateEpochDay,
        dt = dt,
        humidity = humidity,
        windSpeed = windSpeed,
        temp = TempBo(
            day = dayTemp,
            min = minTemp,
            max = maxTemp,
            night = nightTemp
        ),
        weather = listOf(
            WeatherDescriptionBo(
                id = weatherId,
                main = weatherMain,
                description = weatherDescription,
                icon = weatherIcon
            )
        )
    )

fun List<DailyForecastEntity>.toBoList(): List<DailyForecastBo> =
    map { it.toBo() }

