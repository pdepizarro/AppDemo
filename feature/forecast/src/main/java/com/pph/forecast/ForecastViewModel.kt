package com.pph.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pph.domain.usecases.ObserveBarajasForecastUseCase
import com.pph.domain.usecases.RefreshBarajasForecastUseCase
import com.pph.domain.usecases.SaveSelectedDayUseCase
import com.pph.forecast.event.ForecastScreenEvent
import com.pph.forecast.state.ForecastScreenState
import com.pph.shared.ui.mapper.toUiModel
import com.pph.shared.ui.model.DailyForecastUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val refreshBarajasForecastUseCase: RefreshBarajasForecastUseCase,
    private val observeBarajasForecastUseCase: ObserveBarajasForecastUseCase,
    private val saveSelectedDayUseCase: SaveSelectedDayUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        ForecastScreenState(
            isLoading = true,
            forecast = emptyList(),
            errorMessage = null,
            lastSelectedEpochDay = null
        )
    )
    val state: StateFlow<ForecastScreenState> = _state

    private val _events = MutableSharedFlow<ForecastScreenEvent>()
    val events = _events.asSharedFlow()

    init {
        observeForecast()
        refresh()
    }

    // Observe Room: Update viewState when DB changes
    private fun observeForecast() {
        viewModelScope.launch {
            observeBarajasForecastUseCase().collect { boList ->

                delay(2000)

                val today = LocalDate.now()

                val uiList = boList
                    .filter { it.localDate >= today }
                    .take(7)
                    .map { it.toUiModel() }

                _state.value = _state.value.copy(
                    forecast = uiList,
                    isLoading = false
                )
            }
        }
    }


    // Refresh: Fetch data from API and update Room DB
    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = refreshBarajasForecastUseCase()

            result.onFailure { e ->
                val msg = e.message ?: "No se pudo actualizar el pronóstico"

                _state.value = _state.value.copy(errorMessage = msg)
                _events.emit(ForecastScreenEvent.ShowRefreshError(msg))
            }
        }
    }

    fun onDaySelected(day: DailyForecastUiModel) {
        _state.value = _state.value.copy(
            lastSelectedEpochDay = day.date.toEpochDay()
        )

        viewModelScope.launch {
            saveSelectedDayUseCase(day.date.toEpochDay())
            _events.emit(ForecastScreenEvent.NavigateToDetail)
        }
    }
}