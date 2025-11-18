package com.pph.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pph.details.state.ForecastDetailScreenState
import com.pph.domain.usecases.GetSelectedDayUseCase
import com.pph.domain.usecases.ObserveBarajasForecastUseCase
import com.pph.shared.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastDetailsViewModel @Inject constructor(
    private val getSelectedDayUseCase: GetSelectedDayUseCase,
    private val observeBarajasForecastUseCase: ObserveBarajasForecastUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ForecastDetailScreenState())
    val state: StateFlow<ForecastDetailScreenState> = _state

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val selectedEpochDay = getSelectedDayUseCase() ?: return@launch

            observeBarajasForecastUseCase().collect { boList ->

                val uiList = boList.map { it.toUiModel() }

                val selected = uiList.firstOrNull {
                    it.date.toEpochDay() == selectedEpochDay
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    detail = selected,
                    errorMessage = null
                )
            }
        }
    }
}