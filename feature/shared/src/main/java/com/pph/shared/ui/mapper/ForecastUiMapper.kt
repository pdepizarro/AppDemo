package com.pph.shared.ui.mapper

import com.pph.domain.model.DailyForecastBo
import com.pph.shared.ui.model.DailyForecastUiModel
import java.time.Instant
import java.time.ZoneOffset

fun List<DailyForecastBo>.toUiModel(): List<DailyForecastUiModel> =
    map { it.toUiModel() }

fun DailyForecastBo.toUiModel(): DailyForecastUiModel =
    DailyForecastUiModel(
        date = Instant.ofEpochSecond(dt)
            .atOffset(ZoneOffset.UTC)
            .toLocalDate(),
        minTemp = temp.min,
        maxTemp = temp.max,
        dayTemp = temp.day,
        nightTemp = temp.night,
        humidity = humidity,
        windSpeed = windSpeed,
        main = weather.firstOrNull()?.main.orEmpty(),
        description = weather.firstOrNull()?.description.orEmpty(),
        icon = weather.firstOrNull()?.icon.orEmpty()
    )
