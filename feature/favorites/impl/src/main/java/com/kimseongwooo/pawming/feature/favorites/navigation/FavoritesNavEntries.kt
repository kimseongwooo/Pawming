package com.kimseongwooo.pawming.feature.favorites.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kimseongwooo.pawming.feature.favorites.FavoritesRoute
import com.kimseongwooo.pawming.feature.favorites.FavoritesScreen
import com.kimseongwooo.pawming.feature.favorites.FavoritesSideEffect
import com.kimseongwooo.pawming.feature.favorites.FavoritesViewModel

fun EntryProviderScope<NavKey>.favoritesNavEntries(
    onNavigateToAnimalDetail: (desertionNo: String) -> Unit
) {
    entry<FavoritesRoute> {
        val viewModel: FavoritesViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is FavoritesSideEffect.NavigateToAnimalDetail ->
                        onNavigateToAnimalDetail(effect.desertionNo)
                }
            }
        }

        FavoritesScreen(
            uiState = uiState,
            onIntent = viewModel::handleIntent
        )
    }
}
