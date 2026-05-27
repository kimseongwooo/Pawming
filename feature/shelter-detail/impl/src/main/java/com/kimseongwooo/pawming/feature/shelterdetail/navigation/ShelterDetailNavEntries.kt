package com.kimseongwooo.pawming.feature.shelterdetail.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kimseongwooo.pawming.feature.shelterdetail.ShelterDetailIntent
import com.kimseongwooo.pawming.feature.shelterdetail.ShelterDetailRoute
import com.kimseongwooo.pawming.feature.shelterdetail.ShelterDetailScreen
import com.kimseongwooo.pawming.feature.shelterdetail.ShelterDetailSideEffect
import com.kimseongwooo.pawming.feature.shelterdetail.ShelterDetailViewModel

fun EntryProviderScope<NavKey>.shelterDetailNavEntries(
    onBack: () -> Unit,
    onViewAnimalsWithShelterFilter: (careRegNo: String, careNm: String) -> Unit = { _, _ -> }
) {
    entry<ShelterDetailRoute> { route ->
        val viewModel = hiltViewModel<ShelterDetailViewModel, ShelterDetailViewModel.Factory>(
            creationCallback = { factory -> factory.create(route.careRegNo) }
        )
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        LaunchedEffect(viewModel) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    ShelterDetailSideEffect.Back -> onBack()
                    is ShelterDetailSideEffect.Dial -> {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${effect.phoneNumber}"))
                        context.startActivity(intent)
                    }
                    is ShelterDetailSideEffect.NavigateToAnimalsWithShelterFilter -> {
                        onViewAnimalsWithShelterFilter(effect.careRegNo, effect.careNm)
                    }
                }
            }
        }

        ShelterDetailScreen(
            uiState = uiState,
            onIntent = viewModel::handleIntent
        )
    }
}
