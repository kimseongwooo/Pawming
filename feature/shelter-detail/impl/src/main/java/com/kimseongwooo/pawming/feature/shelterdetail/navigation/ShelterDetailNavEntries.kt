package com.kimseongwooo.pawming.feature.shelterdetail.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.kimseongwooo.pawming.feature.shelterdetail.ShelterDetailRoute

fun EntryProviderBuilder<*>.shelterDetailNavEntries(
    onBack: () -> Unit
) {
    entry<ShelterDetailRoute> { route ->
        ShelterDetailScreen(
            careRegNo = route.careRegNo,
            onBack = onBack
        )
    }
}

@Composable
internal fun ShelterDetailScreen(
    careRegNo: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "보호소 상세\n$careRegNo")
    }
}
