package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.AnimalRepository
import com.kimseongwooo.pawming.model.Kind
import javax.inject.Inject

class GetKindUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(upKindCd: String? = null): Result<List<Kind>> =
        animalRepository.getKind(upKindCd)
}
