package com.pph.forecast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pph.uinavigation.ScreenComposable

class ForecastScreenComposable(
    private val onDayDetailNavigation: () -> Unit
) : ScreenComposable {
    @Composable
    override fun Create() {
        val vm: ForecastViewModel = hiltViewModel()
        val state by vm.state.collectAsState()

        ForecastScreen(
            vm = vm,
            state = state,
            onDayDetailNavigation = { onDayDetailNavigation() },
            onRetryClick = { vm.refresh() }
        )
    }
}