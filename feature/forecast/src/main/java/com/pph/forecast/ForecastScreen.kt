package com.pph.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.pph.forecast.components.ForecastScrollableContent
import com.pph.forecast.state.ForecastScreenState
import com.pph.shared.ui.component.ErrorComponent
import com.pph.shared.ui.component.LoadingComponent
import com.pph.shared.ui.model.DailyForecastUiModel

@Composable
fun ForecastScreen(
    state: ForecastScreenState,
    onDayClick: (DailyForecastUiModel) -> Unit,
    onRetryClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val squareSize = configuration.screenHeightDp.dp / 5

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101018))
    ) {
        when {
            state.isLoading -> {
                LoadingComponent()
            }

            state.errorMessage != null -> {
                ErrorComponent(
                    message = state.errorMessage,
                    onRetryClick = onRetryClick
                )
            }

            else -> {
                ForecastScrollableContent(
                    forecast = state.forecast,
                    squareSize = squareSize,
                    onDayClick = onDayClick
                )
            }
        }
    }
}