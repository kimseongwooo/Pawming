package com.kimseongwooo.pawming.domain.repository

import com.kimseongwooo.pawming.model.FavoriteAnimal
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<FavoriteAnimal>>
    fun getFavoriteIds(): Flow<Set<String>>
    suspend fun addFavorite(animal: FavoriteAnimal)
    suspend fun removeFavorite(desertionNo: String)
}
