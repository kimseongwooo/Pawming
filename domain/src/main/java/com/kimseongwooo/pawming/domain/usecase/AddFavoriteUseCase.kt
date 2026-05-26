package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.FavoriteRepository
import com.kimseongwooo.pawming.model.FavoriteAnimal
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(animal: FavoriteAnimal) = favoriteRepository.addFavorite(animal)
}
