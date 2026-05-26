package com.kimseongwooo.pawming.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kimseongwooo.pawming.designsystem.component.AnimalCard
import com.kimseongwooo.pawming.designsystem.component.PawmingLoadingIndicator
import com.kimseongwooo.pawming.feature.home.HomeIntent
import com.kimseongwooo.pawming.feature.home.HomeUiState

@Composable
internal fun AnimalGrid(
    uiState: HomeUiState,
    gridState: LazyGridState,
    onIntent: (HomeIntent) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = uiState.animals, key = { it.desertionNo }) { animal ->
            AnimalCard(
                processState = animal.processState,
                kindNm = animal.kindNm,
                sexCd = animal.sexCd,
                age = animal.age,
                happenPlace = animal.happenPlace,
                isFavorite = false,
                onClick = { onIntent(HomeIntent.ClickAnimal(animal.desertionNo)) },
                onFavoriteClick = {}
            ) {
                AnimalThumbnail(imageUrl = animal.images.firstOrNull())
            }
        }
        if (uiState.isLoadingMore) {
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PawmingLoadingIndicator()
                }
            }
        }
        if (!uiState.hasMore && uiState.animals.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "모든 동물을 불러왔어요 🐾",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color(0xFFCCCCCC)
                )
            }
        }
    }
}

@Composable
internal fun AnimalThumbnail(imageUrl: String?) {
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🐾", fontSize = 32.sp)
        }
    }
}
