package com.pph.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pph.domain.usecases.GetBarajasForecastUseCase
import com.pph.forecast.state.ForecastScreenState
import com.pph.shared.ui.mapper.toUiModel
import com.pph.shared.ui.model.DailyForecastUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getBarajasForecastUseCase: GetBarajasForecastUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        ForecastScreenState(
            isLoading = true,
            forecast = emptyList(),
            errorMessage = null
        )
    )
    val state: StateFlow<ForecastScreenState> get() = _state

    init {
        loadForecastData()
    }

    fun loadForecastData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = getBarajasForecastUseCase()

            _state.value = result.fold(
                onSuccess = { responseBo ->
                    val uiList = responseBo.daily
                        .take(7)
                        .map { it.toUiModel() }

                    _state.value.copy(
                        isLoading = false,
                        forecast = uiList,
                        errorMessage = null
                    )
                },
                onFailure = { throwable ->
                    val fakeList = generateFakeForecast(LocalDate.now())

                    _state.value.copy(
                        isLoading = false,
                        forecast = fakeList,
                        errorMessage = throwable.message ?: "Error al cargar el pronóstico"
                    )
                }
            )
        }
    }

    private fun generateFakeForecast(today: LocalDate): List<DailyForecastUiModel> =
        (0 until 7).map { index ->
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
}