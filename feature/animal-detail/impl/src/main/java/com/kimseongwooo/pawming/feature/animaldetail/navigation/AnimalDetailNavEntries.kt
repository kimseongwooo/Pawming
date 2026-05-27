package com.kimseongwooo.pawming.feature.animaldetail.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kimseongwooo.pawming.feature.animaldetail.AnimalDetailRoute
import com.kimseongwooo.pawming.feature.animaldetail.AnimalDetailScreen
import com.kimseongwooo.pawming.feature.animaldetail.AnimalDetailSideEffect
import com.kimseongwooo.pawming.feature.animaldetail.AnimalDetailViewModel

fun EntryProviderScope<NavKey>.animalDetailNavEntries(
    onBack: () -> Unit
) {
    entry<AnimalDetailRoute> { route ->
        val viewModel = hiltViewModel<AnimalDetailViewModel, AnimalDetailViewModel.Factory>(
            creationCallback = { factory -> factory.create(route.desertionNo) }
        )
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    AnimalDetailSideEffect.NavigateBack -> onBack()
                }
            }
        }

        AnimalDetailScreen(
            uiState = uiState,
            onIntent = viewModel::handleIntent,
            onBack = onBack
        )
    }
}
