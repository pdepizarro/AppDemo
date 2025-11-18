package com.pph.forecast.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.pph.shared.ui.model.DailyForecastUiModel
import com.pph.uicomponents.theme.DemoAppTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ForecastDayItem(
    forecast: DailyForecastUiModel,
    squareSize: Dp,
    scale: Float,
    onCenterRequest: () -> Unit,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            ),
        contentAlignment = Alignment.Center
    ) {
        val isSelected = scale > 1f

        val dateFormatter = DateTimeFormatter.ofPattern("EEE d MMM", Locale.getDefault())

        val backgroundColor =
            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surface

        Card(
            modifier = Modifier
                .size(squareSize)
                .clickable {
                    onCenterRequest()
                    onClick()
                },
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = DemoAppTheme.dimens.x200)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(DemoAppTheme.dimens.x300),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = forecast.date.format(dateFormatter),
                    style = DemoAppTheme.typography.bodyMedium
                )

                Text(
                    text = iconToEmoji(forecast.icon),
                    style = DemoAppTheme.typography.headlineMedium
                )

                Text(
                    text = "${forecast.maxTemp}º / ${forecast.minTemp}º",
                    style = DemoAppTheme.typography.bodyLarge
                )
            }
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
        else -> "❓"
    }
