package com.kimseongwooo.pawming.feature.animaldetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kimseongwooo.pawming.designsystem.component.PawmingErrorContent
import com.kimseongwooo.pawming.designsystem.component.PawmingLoadingIndicator
import com.kimseongwooo.pawming.feature.animaldetail.components.AdoptionSection
import com.kimseongwooo.pawming.feature.animaldetail.components.BackButton
import com.kimseongwooo.pawming.feature.animaldetail.components.BasicInfoSection
import com.kimseongwooo.pawming.feature.animaldetail.components.FavoriteBottomBar
import com.kimseongwooo.pawming.feature.animaldetail.components.HealthSection
import com.kimseongwooo.pawming.feature.animaldetail.components.ImageGallery
import com.kimseongwooo.pawming.feature.animaldetail.components.NameSection
import com.kimseongwooo.pawming.feature.animaldetail.components.NoticeSection
import com.kimseongwooo.pawming.feature.animaldetail.components.ShelterSection
import com.kimseongwooo.pawming.model.Animal

@Composable
internal fun AnimalDetailScreen(
    uiState: AnimalDetailUiState,
    onIntent: (AnimalDetailIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    PawmingLoadingIndicator()
                }
                BackButton(onBack = onBack)
            }
            uiState.error != null -> {
                PawmingErrorContent(
                    message = uiState.error,
                    onRetry = { onIntent(AnimalDetailIntent.Retry) },
                    modifier = Modifier.fillMaxSize()
                )
                BackButton(onBack = onBack)
            }
            uiState.animal != null -> {
                AnimalDetailContent(
                    animal = uiState.animal,
                    isFavorite = uiState.isFavorite,
                    shelterLat = uiState.shelterLat,
                    shelterLng = uiState.shelterLng,
                    isShelterLoading = uiState.isShelterLoading,
                    onToggleFavorite = { onIntent(AnimalDetailIntent.ToggleFavorite) },
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun AnimalDetailContent(
    animal: Animal,
    isFavorite: Boolean,
    shelterLat: Double?,
    shelterLng: Double?,
    isShelterLoading: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                ImageGallery(
                    images = animal.images,
                    processState = animal.processState,
                    onBack = onBack
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
            item { NameSection(animal) }
            item { Spacer(Modifier.height(8.dp)) }
            item { NoticeSection(animal) }
            item { Spacer(Modifier.height(8.dp)) }
            item { BasicInfoSection(animal) }
            item { Spacer(Modifier.height(8.dp)) }
            item { HealthSection(animal) }
            if (animal.adptnTitle.isNotEmpty()) {
                item { Spacer(Modifier.height(8.dp)) }
                item { AdoptionSection(animal) }
            }
            item { Spacer(Modifier.height(8.dp)) }
            item { ShelterSection(animal = animal, shelterLat = shelterLat, shelterLng = shelterLng, isShelterLoading = isShelterLoading) }
        }

        FavoriteBottomBar(
            isFavorite = isFavorite,
            onClick = onToggleFavorite,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
