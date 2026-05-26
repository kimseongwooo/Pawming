package com.kimseongwooo.pawming.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.kimseongwooo.pawming.designsystem.component.PawmingEmptyContent
import com.kimseongwooo.pawming.designsystem.component.PawmingErrorContent
import com.kimseongwooo.pawming.feature.home.components.AnimalGrid
import com.kimseongwooo.pawming.feature.home.components.HomeHeader
import com.kimseongwooo.pawming.feature.home.components.ShelterDetailBottomSheet
import com.kimseongwooo.pawming.feature.home.components.ShelterPickerBottomSheet
import com.kimseongwooo.pawming.feature.home.components.SkeletonGrid
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import androidx.compose.runtime.snapshotFlow

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo }
            .map { info ->
                val last = info.visibleItemsInfo.lastOrNull()?.index ?: return@map false
                last >= info.totalItemsCount - 4
            }
            .distinctUntilChanged()
            .collect { nearEnd ->
                if (nearEnd) onIntent(HomeIntent.LoadMore)
            }
    }

    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader(uiState = uiState, onIntent = onIntent)
        Box(modifier = Modifier.weight(1f)) {
            when {
                uiState.isLoading -> SkeletonGrid()
                uiState.error != null -> PawmingErrorContent(
                    message = "정보를 불러오지 못했어요",
                    description = uiState.error,
                    onRetry = { onIntent(HomeIntent.LoadInitial) }
                )

                uiState.animals.isEmpty() -> PawmingEmptyContent(
                    emoji = "🔍",
                    message = "조건에 맞는 동물이 없습니다",
                    action = if (uiState.hasFilter) {
                        {
                            TextButton(onClick = { onIntent(HomeIntent.ResetFilters) }) {
                                Text("필터 초기화", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    } else null
                )

                else -> AnimalGrid(uiState = uiState, gridState = gridState, onIntent = onIntent)
            }
        }
    }

    if (uiState.isShowShelterPicker) {
        ShelterPickerBottomSheet(state = uiState, onIntent = onIntent)
    }

    if (uiState.shelterDetail != null || uiState.isLoadingShelterDetail) {
        ShelterDetailBottomSheet(uiState = uiState, onIntent = onIntent)
    }
}
