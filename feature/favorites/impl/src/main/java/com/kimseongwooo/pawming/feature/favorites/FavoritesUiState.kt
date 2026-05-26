package com.kimseongwooo.pawming.feature.favorites

import androidx.compose.runtime.Immutable
import com.kimseongwooo.pawming.model.FavoriteAnimal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FavoritesUiState(
    val favorites: ImmutableList<FavoriteAnimal> = persistentListOf(),
    val isLoading: Boolean = true
)

sealed interface FavoritesIntent {
    data class ClickAnimal(val desertionNo: String) : FavoritesIntent
    data class RemoveFavorite(val desertionNo: String) : FavoritesIntent
}

sealed interface FavoritesSideEffect {
    data class NavigateToAnimalDetail(val desertionNo: String) : FavoritesSideEffect
}
