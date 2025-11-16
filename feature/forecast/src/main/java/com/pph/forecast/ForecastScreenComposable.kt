package com.pph.forecast

import androidx.compose.runtime.Composable
import com.pph.uinavigation.ScreenComposable

class ForecastScreenComposable : ScreenComposable {
    @Composable
    override fun Create() {
        ForecastScreen(onDayClick = {})
    }
}