package com.pph.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pph.forecast.components.ForecastDaySquare
import com.pph.shared.ui.model.DailyForecastUiModel
import java.time.LocalDate

@Composable
fun ForecastScreen(
    onDayClick: (DailyForecastUiModel) -> Unit
) {
    val vm: ForecastViewModel = hiltViewModel()
    val state by vm.state.collectAsState()


    val configuration = LocalConfiguration.current
    val squareSize = configuration.screenHeightDp.dp / 5

    if(state.isLoading){

    }else{
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF101018)),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.forecast,
                key = { it.date.toEpochDay() } // opcional, pero ayuda a la perf
            ) { forecast ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    ForecastDaySquare(
                        forecast = forecast,
                        size = squareSize,
                        onClick = { onDayClick(forecast) }
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForecastListScreenPreview() {
    val today = LocalDate.now()
    val fakeForecasts = (0 until 7).map { index ->
        DailyForecastUiModel(
            date = today.plusDays(index.toLong()),
            minTemp = 10 + index,
            maxTemp = 18 + index,
            humidity = 60 + index,
            windSpeed = 10 + index,
            condition = when (index % 3) {
                0 -> "Soleado"
                1 -> "Nublado"
                else -> "Lluvia"
            }
        )
    }

    MaterialTheme {
        Surface {
            ForecastScreen(
                onDayClick = { /* Navegación a detalle */ }
            )
        }
    }
}