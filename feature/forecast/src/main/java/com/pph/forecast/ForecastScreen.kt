package com.pph.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.pph.forecast.components.ForecastScrollableContent
import com.pph.forecast.event.ForecastScreenEvent
import com.pph.forecast.state.ForecastScreenState
import com.pph.shared.ui.component.ErrorComponent
import com.pph.shared.ui.component.ErrorDialog
import com.pph.shared.ui.component.LoadingComponent


@Composable
fun ForecastScreen(
    vm: ForecastViewModel,
    state: ForecastScreenState,
    onDayDetailNavigation: () -> Unit,
    onRetryClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val squareSize = configuration.screenHeightDp.dp / 5

    var dialogMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        vm.events.collect { event ->
            when (event) {
                ForecastScreenEvent.NavigateToDetail -> onDayDetailNavigation()
                is ForecastScreenEvent.ShowRefreshError -> {
                    dialogMessage = event.message
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        when {
            state.isLoading -> LoadingComponent()

            state.forecast.isEmpty() && !state.isLoading -> {
                ErrorComponent(
                    message = state.errorMessage ?: "No hay información disponible",
                    onRetryClick = onRetryClick
                )
            }

            else -> {
                ForecastScrollableContent(
                    vm = vm,
                    forecast = state.forecast,
                    squareSize = squareSize
                )
            }
        }

        if (dialogMessage != null && state.forecast.isNotEmpty()) {
            ErrorDialog(
                message = dialogMessage!!,
                onDismiss = { dialogMessage = null }
            )
        }
    }
}
