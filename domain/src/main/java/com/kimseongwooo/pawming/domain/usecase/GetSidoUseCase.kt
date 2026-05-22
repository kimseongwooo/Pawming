package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.AnimalRepository
import com.kimseongwooo.pawming.model.Sido
import javax.inject.Inject

class GetSidoUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(): Result<List<Sido>> =
        animalRepository.getSido()
}
