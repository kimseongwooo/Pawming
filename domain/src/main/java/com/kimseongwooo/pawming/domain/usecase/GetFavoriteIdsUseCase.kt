package com.kimseongwooo.pawming.domain.usecase

import com.kimseongwooo.pawming.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteIdsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(): Flow<Set<String>> = favoriteRepository.getFavoriteIds()
}
