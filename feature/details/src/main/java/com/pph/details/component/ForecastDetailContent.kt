package com.pph.details.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.pph.shared.ui.model.DailyForecastUiModel
import com.pph.uicomponents.theme.DemoAppTheme
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.pph.details.R

@Composable
fun ForecastDetailContent(
    forecast: DailyForecastUiModel
) {
    val datePattern = stringResource(id = R.string.forecast_detail_date_pattern)
    val dateFormatter = remember(datePattern) {
        DateTimeFormatter.ofPattern(datePattern, Locale.getDefault())
    }

    val windKmh = remember(forecast.windSpeed) {
        (forecast.windSpeed * 3.6).toInt()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = DemoAppTheme.dimens.x400,
                vertical = DemoAppTheme.dimens.x300
            )
    ) {
        Text(
            text = forecast.date.format(dateFormatter),
            style = DemoAppTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(DemoAppTheme.dimens.x300))

        Text(
            text = forecast.description.replaceFirstChar { it.uppercase() },
            style = DemoAppTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(DemoAppTheme.dimens.x300))

        Text(
            text = stringResource(
                id = R.string.forecast_detail_max_temp,
                forecast.maxTemp
            ),
            style = DemoAppTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(
                id = R.string.forecast_detail_min_temp,
                forecast.minTemp
            ),
            style = DemoAppTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(DemoAppTheme.dimens.x200))

        Text(
            text = stringResource(
                id = R.string.forecast_detail_day_temp,
                forecast.dayTemp
            ),
            style = DemoAppTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(
                id = R.string.forecast_detail_night_temp,
                forecast.nightTemp
            ),
            style = DemoAppTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(DemoAppTheme.dimens.x200))

        Text(
            text = stringResource(
                id = R.string.forecast_detail_humidity,
                forecast.humidity
            ),
            style = DemoAppTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(
                id = R.string.forecast_detail_wind,
                windKmh
            ),
            style = DemoAppTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(DemoAppTheme.dimens.x800))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = DemoAppTheme.dimens.x700),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconToEmoji(forecast.icon),
                fontSize = DemoAppTheme.dimens.x2400.value.sp
            )
        }
    }
}

private fun iconToEmoji(icon: String): String =
    when (icon) {
        "01d", "01n" -> "☀️"
        "02d", "02n" -> "🌤️"
        "03d", "03n", "04d", "04n" -> "☁️"
        "09d", "09n" -> "🌧️"
        "10d", "10n" -> "🌦️"
        "11d", "11n" -> "⛈️"
        "13d", "13n" -> "❄️"
        "50d", "50n" -> "🌫️"
        else -> "️❓"
    }