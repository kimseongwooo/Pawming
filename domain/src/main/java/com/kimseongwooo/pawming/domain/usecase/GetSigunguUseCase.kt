package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.AnimalRepository
import com.kimseongwooo.pawming.model.Sigungu
import javax.inject.Inject

class GetSigunguUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(uprCd: String): Result<List<Sigungu>> =
        animalRepository.getSigungu(uprCd)
}
