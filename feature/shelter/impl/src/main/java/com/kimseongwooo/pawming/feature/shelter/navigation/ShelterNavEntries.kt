package com.kimseongwooo.pawming.feature.shelter.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kimseongwooo.pawming.feature.shelter.ShelterRoute
import com.kimseongwooo.pawming.feature.shelter.ShelterScreen
import com.kimseongwooo.pawming.feature.shelter.ShelterSideEffect
import com.kimseongwooo.pawming.feature.shelter.ShelterViewModel

fun EntryProviderScope<NavKey>.shelterNavEntries(
    onNavigateToShelterDetail: (careRegNo: String) -> Unit,
    metadata: Map<String, Any> = emptyMap()
) {
    entry<ShelterRoute>(metadata = metadata) {
        val viewModel: ShelterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is ShelterSideEffect.NavigateToShelterDetail ->
                        onNavigateToShelterDetail(effect.careRegNo)
                }
            }
        }

        ShelterScreen(
            uiState = uiState,
            onIntent = viewModel::handleIntent
        )
    }
}
