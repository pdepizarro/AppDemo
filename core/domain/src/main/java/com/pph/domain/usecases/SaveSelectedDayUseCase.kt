package com.pph.domain.usecases

import com.pph.domain.repository.SelectedDayRepository
import javax.inject.Inject

class SaveSelectedDayUseCase @Inject constructor(
    private val repo: SelectedDayRepository
) {
    suspend operator fun invoke(epochDay: Long) = repo.saveSelectedDay(epochDay)
}