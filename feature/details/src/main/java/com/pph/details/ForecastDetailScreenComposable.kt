package com.pph.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pph.uinavigation.ScreenComposable

class ForecastDetailScreenComposable(
) : ScreenComposable {
    @Composable
    override fun Create() {
        val vm: ForecastDetailsViewModel = hiltViewModel()
        val state by vm.state.collectAsState()

        ForecastDetailScreen(
            state = state,
            onRetryClick = {},
        )
    }
}