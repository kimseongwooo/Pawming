package com.kimseongwooo.pawming.feature.animaldetail.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.kimseongwooo.pawming.feature.animaldetail.AnimalDetailRoute

fun EntryProviderBuilder.animalDetailNavEntries(
    onBack: () -> Unit
) {
    entry<AnimalDetailRoute> { route ->
        AnimalDetailScreen(
            desertionNo = route.desertionNo,
            onBack = onBack
        )
    }
}

@Composable
internal fun AnimalDetailScreen(
    desertionNo: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "동물 상세\n$desertionNo")
    }
}
