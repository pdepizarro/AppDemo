package com.pph.domain.usecases

import com.pph.domain.repository.SelectedDayRepository
import javax.inject.Inject

class GetSelectedDayUseCase @Inject constructor(
    private val repo: SelectedDayRepository
) {
    suspend operator fun invoke(): Long? = repo.getSelectedDay()
}