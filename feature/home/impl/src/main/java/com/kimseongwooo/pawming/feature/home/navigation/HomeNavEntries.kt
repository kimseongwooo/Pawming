package com.kimseongwooo.pawming.feature.home.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kimseongwooo.pawming.feature.home.HomeSideEffect
import com.kimseongwooo.pawming.feature.home.HomeScreen
import com.kimseongwooo.pawming.feature.home.HomeViewModel
import com.kimseongwooo.pawming.feature.home.HomeRoute
import com.kimseongwooo.pawming.feature.home.HomeIntent

fun EntryProviderScope<NavKey>.homeNavEntries(
    onNavigateToAnimalDetail: (desertionNo: String) -> Unit,
    pendingShelterId: String = "",
    pendingShelterNm: String = "",
    onShelterFilterApplied: () -> Unit = {},
    metadata: Map<String, Any> = emptyMap()
) {
    entry<HomeRoute>(metadata = metadata) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is HomeSideEffect.NavigateToAnimalDetail ->
                        onNavigateToAnimalDetail(effect.desertionNo)
                }
            }
        }

        LaunchedEffect(pendingShelterId) {
            if (pendingShelterId.isNotEmpty()) {
                viewModel.handleIntent(
                    HomeIntent.ApplyExternalShelterFilter(
                        careRegNo = pendingShelterId,
                        careNm = pendingShelterNm
                    )
                )
                onShelterFilterApplied()
            }
        }

        HomeScreen(
            uiState = state,
            onIntent = viewModel::handleIntent
        )
    }
}
