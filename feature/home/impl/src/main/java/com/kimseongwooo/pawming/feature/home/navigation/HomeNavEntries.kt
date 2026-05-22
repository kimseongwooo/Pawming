package com.kimseongwooo.pawming.feature.home.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.kimseongwooo.pawming.feature.home.HomeRoute

fun EntryProviderBuilder<*>.homeNavEntries(
    onNavigateToAnimalDetail: (desertionNo: String) -> Unit
) {
    entry<HomeRoute> {
        HomeScreen(onNavigateToAnimalDetail = onNavigateToAnimalDetail)
    }
}

@Composable
internal fun HomeScreen(
    onNavigateToAnimalDetail: (desertionNo: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "홈")
    }
}
