package com.kimseongwooo.pawming.feature.shelterdetail

import androidx.compose.runtime.Immutable
import com.kimseongwooo.pawming.model.ShelterDetail

@Immutable
data class ShelterDetailUiState(
    val shelterDetail: ShelterDetail? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

sealed interface ShelterDetailIntent {
    data object Retry : ShelterDetailIntent
    data object NavigateBack : ShelterDetailIntent
    data object CallPhone : ShelterDetailIntent
    data object ViewAnimals : ShelterDetailIntent
}

sealed interface ShelterDetailSideEffect {
    data object Back : ShelterDetailSideEffect
    data class Dial(val phoneNumber: String) : ShelterDetailSideEffect
    data class NavigateToAnimalsWithShelterFilter(val careRegNo: String, val careNm: String) : ShelterDetailSideEffect
}
