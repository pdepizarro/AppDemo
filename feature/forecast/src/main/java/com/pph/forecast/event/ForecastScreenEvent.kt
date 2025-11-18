package com.pph.forecast.event

sealed class ForecastScreenEvent {
    object NavigateToDetail : ForecastScreenEvent()
    data class ShowRefreshError(val message: String) : ForecastScreenEvent()
}