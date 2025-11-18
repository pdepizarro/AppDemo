package com.pph.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pph.forecast.components.ForecastScrollableContent
import com.pph.forecast.event.ForecastScreenEvent
import com.pph.forecast.state.ForecastScreenState
import com.pph.uicomponents.components.ErrorComponent
import com.pph.uicomponents.components.ErrorDialog
import com.pph.uicomponents.components.LoadingComponent

@Composable
fun ForecastScreen(
    vm: ForecastViewModel,
    state: ForecastScreenState,
    onDayDetailNavigation: () -> Unit,
    onRetryClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    // Square size: Base item size calculated from screen height
    val squareSize = configuration.screenHeightDp.dp / 5

    var dialogMessage by remember { mutableStateOf<String?>(null) }

    // Event collector: Listens to ViewModel events for navigation and errors
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

        // Content state: Decide between loading, error, or forecast content loaded from BD (Room)
        when {
            state.isLoading -> LoadingComponent()

            state.forecast.isEmpty() && !state.isLoading -> {
                ErrorComponent(
                    message = state.errorMessage
                        ?: stringResource(id = R.string.forecast_screen_empty_message),
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

        // Error dialog: Show dialog only when there is a pendant error message and data
        if (dialogMessage != null && state.forecast.isNotEmpty()) {
            ErrorDialog(
                message = dialogMessage ?: stringResource(id = R.string.forecast_screen_refresh_error_default),
                onDismiss = { dialogMessage = null }
            )
        }
    }
}
