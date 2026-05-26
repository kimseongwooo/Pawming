package com.kimseongwooo.pawming.data.repository

import com.kimseongwooo.pawming.data.db.FavoriteAnimalDao
import com.kimseongwooo.pawming.data.mapper.toDomain
import com.kimseongwooo.pawming.data.mapper.toEntity
import com.kimseongwooo.pawming.domain.repository.FavoriteRepository
import com.kimseongwooo.pawming.model.FavoriteAnimal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteAnimalDao
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<FavoriteAnimal>> =
        dao.getFavorites().map { list -> list.map { it.toDomain() } }

    override fun getFavoriteIds(): Flow<Set<String>> =
        dao.getFavoriteIds().map { it.toSet() }

    override suspend fun addFavorite(animal: FavoriteAnimal) = dao.insert(animal.toEntity())

    override suspend fun removeFavorite(desertionNo: String) = dao.delete(desertionNo)
}
