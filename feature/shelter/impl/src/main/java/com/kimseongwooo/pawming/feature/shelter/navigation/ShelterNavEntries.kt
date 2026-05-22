package com.kimseongwooo.pawming.feature.shelter.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.kimseongwooo.pawming.feature.shelter.ShelterRoute

fun EntryProviderBuilder<*>.shelterNavEntries(
    onNavigateToShelterDetail: (careRegNo: String) -> Unit
) {
    entry<ShelterRoute> {
        ShelterScreen(onNavigateToShelterDetail = onNavigateToShelterDetail)
    }
}

@Composable
internal fun ShelterScreen(
    onNavigateToShelterDetail: (careRegNo: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "보호센터")
    }
}
