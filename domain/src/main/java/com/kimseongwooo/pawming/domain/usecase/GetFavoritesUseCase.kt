package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.FavoriteRepository
import com.kimseongwooo.pawming.model.FavoriteAnimal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<FavoriteAnimal>> = favoriteRepository.getFavorites()
}
