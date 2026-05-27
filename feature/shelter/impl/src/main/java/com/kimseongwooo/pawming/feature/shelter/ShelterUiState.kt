package com.kimseongwooo.pawming.feature.shelter

import androidx.compose.runtime.Immutable
import com.kimseongwooo.pawming.model.Shelter
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ShelterUiState(
    val sidoList: ImmutableList<Sido> = persistentListOf(),
    val sigunguList: ImmutableList<Sigungu> = persistentListOf(),
    val selectedSido: Sido? = null,
    val selectedSigungu: Sigungu? = null,
    val shelters: ImmutableList<Shelter> = persistentListOf(),
    val searchQuery: String = "",
    val isSidoLoading: Boolean = false,
    val isShelterLoading: Boolean = false,
    val error: String? = null
)

sealed interface ShelterIntent {
    data class SelectSido(val sido: Sido) : ShelterIntent
    data class SelectSigungu(val sigungu: Sigungu) : ShelterIntent
    data class UpdateSearchQuery(val query: String) : ShelterIntent
    data class ClickShelter(val careRegNo: String) : ShelterIntent
}

sealed interface ShelterSideEffect {
    data class NavigateToShelterDetail(val careRegNo: String) : ShelterSideEffect
}
