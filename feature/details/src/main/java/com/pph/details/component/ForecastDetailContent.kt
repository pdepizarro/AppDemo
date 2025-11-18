package com.pph.details.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.pph.shared.ui.model.DailyForecastUiModel
import com.pph.uicomponents.theme.DemoAppTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ForecastDetailContent(
    forecast: DailyForecastUiModel
) {
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale.getDefault())
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
            text = "Temperatura máxima: ${forecast.maxTemp}º",
            style = DemoAppTheme.typography.bodyLarge
        )

        Text(
            text = "Temperatura mínima: ${forecast.minTemp}º",
            style = DemoAppTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(DemoAppTheme.dimens.x200))

        Text(
            text = "Temperatura día: ${forecast.dayTemp}º",
            style = DemoAppTheme.typography.bodyLarge
        )

        Text(
            text = "Temperatura noche: ${forecast.nightTemp}º",
            style = DemoAppTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(DemoAppTheme.dimens.x200))

        Text(
            text = "Humedad: ${forecast.humidity}%",
            style = DemoAppTheme.typography.bodyLarge
        )

        Text(
            text = "Viento: $windKmh km/h",
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