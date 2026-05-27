package com.kimseongwooo.pawming.feature.shelter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kimseongwooo.pawming.designsystem.component.PawmingEmptyContent
import com.kimseongwooo.pawming.designsystem.theme.PawmingColors
import com.kimseongwooo.pawming.feature.shelter.components.ShelterHeader
import com.kimseongwooo.pawming.feature.shelter.components.ShelterListContent
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ShelterScreen(
    uiState: ShelterUiState,
    onIntent: (ShelterIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ShelterHeader(
            sidoList = uiState.sidoList,
            sigunguList = uiState.sigunguList,
            selectedSido = uiState.selectedSido,
            selectedSigungu = uiState.selectedSigungu,
            searchQuery = uiState.searchQuery,
            isInitialLoading = uiState.isInitialLoading,
            onSelectSido = { onIntent(ShelterIntent.SelectSido(it)) },
            onSelectSigungu = { onIntent(ShelterIntent.SelectSigungu(it)) },
            onSearchQueryChange = { onIntent(ShelterIntent.UpdateSearchQuery(it)) }
        )

        when {
            uiState.isInitialLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            uiState.selectedSigungu == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "시/도와 시/군/구를 선택하면\n보호센터 목록을 볼 수 있어요",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PawmingColors.Neutral400,
                        textAlign = TextAlign.Center
                    )
                }
            }
            uiState.isShelterLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PawmingColors.Neutral500
                    )
                }
            }
            uiState.shelters.isEmpty() -> {
                PawmingEmptyContent(
                    emoji = "🏠",
                    message = "등록된 보호센터가 없어요",
                    description = "해당 지역에 등록된\n동물보호센터 정보가 없습니다",
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                val filtered = remember(uiState.shelters, uiState.searchQuery) {
                    if (uiState.searchQuery.isBlank()) uiState.shelters
                    else uiState.shelters.filter { shelter ->
                        shelter.careNm.contains(uiState.searchQuery, ignoreCase = true) ||
                            shelter.careAddr.contains(uiState.searchQuery, ignoreCase = true)
                    }.toImmutableList()
                }
                if (filtered.isEmpty()) {
                    PawmingEmptyContent(
                        emoji = "🔍",
                        message = "검색 결과가 없어요",
                        description = "\"${uiState.searchQuery}\"와 일치하는\n보호센터를 찾지 못했습니다",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ShelterListContent(
                        shelters = filtered,
                        totalCount = uiState.shelters.size,
                        onClickShelter = { onIntent(ShelterIntent.ClickShelter(it)) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
