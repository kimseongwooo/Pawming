package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.FavoriteRepository
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(desertionNo: String) = favoriteRepository.removeFavorite(desertionNo)
}
