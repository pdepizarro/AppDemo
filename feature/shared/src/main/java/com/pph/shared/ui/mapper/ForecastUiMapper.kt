package com.pph.shared.ui.mapper

import com.pph.domain.model.DailyForecastBo
import com.pph.domain.model.ForecastResponseBo
import com.pph.shared.ui.model.DailyForecastUiModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun ForecastResponseBo.toUiModel(): List<DailyForecastUiModel> =
    daily.map { it.toUiModel() }

fun DailyForecastBo.toUiModel(): DailyForecastUiModel {
    val localDate: LocalDate =
        Instant.ofEpochSecond(dt)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

    val conditionMain = weather.firstOrNull()?.main ?: "Desconocido"

    return DailyForecastUiModel(
        date = localDate,
        minTemp = temp.min.toInt(),
        maxTemp = temp.max.toInt(),
        humidity = humidity,
        windSpeed = windSpeed.toInt(),
        condition = conditionMain
    )
}
