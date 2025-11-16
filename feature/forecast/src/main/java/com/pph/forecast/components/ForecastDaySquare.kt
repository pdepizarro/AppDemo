package com.pph.forecast.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pph.shared.ui.model.DailyForecastUiModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ForecastDaySquare(
    forecast: DailyForecastUiModel,
    size: Dp,
    onClick: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEE d MMM", Locale.getDefault())

    Card(
        modifier = Modifier
            .size(size) // cuadrado: alto = ancho = 1/5 de la altura de la pantalla
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E2C)
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

            // Temperaturas
            Text(
                text = "${forecast.maxTemp}º / ${forecast.minTemp}º",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
        }
    }
}