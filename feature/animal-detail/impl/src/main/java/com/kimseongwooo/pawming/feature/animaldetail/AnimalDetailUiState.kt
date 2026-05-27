package com.kimseongwooo.pawming.feature.animaldetail

import androidx.compose.runtime.Immutable
import com.kimseongwooo.pawming.model.Animal

@Immutable
data class AnimalDetailUiState(
    val animal: Animal? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isFavorite: Boolean = false
)

sealed interface AnimalDetailIntent {
    data object ToggleFavorite : AnimalDetailIntent
    data object Retry : AnimalDetailIntent
}

sealed interface AnimalDetailSideEffect {
    data object NavigateBack : AnimalDetailSideEffect
}
