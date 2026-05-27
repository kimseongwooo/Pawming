package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.AnimalRepository
import com.kimseongwooo.pawming.model.Animal
import javax.inject.Inject

class GetAnimalByDesertionNoUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(desertionNo: String): Result<Animal?> =
        animalRepository.getAnimalByDesertionNo(desertionNo)
}
