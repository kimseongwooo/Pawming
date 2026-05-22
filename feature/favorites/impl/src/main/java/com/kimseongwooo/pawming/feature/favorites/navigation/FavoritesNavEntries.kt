package com.kimseongwooo.pawming.feature.favorites.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.kimseongwooo.pawming.feature.favorites.FavoritesRoute

fun EntryProviderBuilder.favoritesNavEntries(
    onNavigateToAnimalDetail: (desertionNo: String) -> Unit
) {
    entry<FavoritesRoute> {
        FavoritesScreen(onNavigateToAnimalDetail = onNavigateToAnimalDetail)
    }
}

@Composable
internal fun FavoritesScreen(
    onNavigateToAnimalDetail: (desertionNo: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "즐겨찾기")
    }
}
