package com.pph.forecast.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pph.shared.ui.model.DailyForecastUiModel
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
            if (isSelected) Color(0xFF2E2E4B)
            else Color(0xFF1E1E2C)

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
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = forecast.date.format(dateFormatter),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Text(
                    text = when (forecast.condition.lowercase()) {
                        "soleado" -> "☀️"
                        "nublado" -> "☁️"
                        "lluvia" -> "🌧️"
                        "tormenta" -> "⛈️"
                        "nieve" -> "❄️"
                        else -> "🌤️"
                    },
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "${forecast.maxTemp}º / ${forecast.minTemp}º",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }
        }
    }
}
