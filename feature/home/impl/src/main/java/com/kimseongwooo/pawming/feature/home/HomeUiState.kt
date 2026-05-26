package com.kimseongwooo.pawming.feature.home

import androidx.compose.runtime.Immutable
import com.kimseongwooo.pawming.model.Animal
import com.kimseongwooo.pawming.model.Shelter
import com.kimseongwooo.pawming.model.ShelterDetail
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

enum class ShelterPickerStep { SIDO, SIGUNGU, SHELTER }

@Immutable
data class HomeUiState(
    val animals: ImmutableList<Animal> = persistentListOf(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val currentPage: Int = 1,
    val error: String? = null,
    val isFilterOpen: Boolean = false,
    val filterUpkind: String = "",
    val filterNeuter: String = "",
    val filterState: String = "",
    val filterCareRegNo: String = "",
    val filterCareNm: String = "",
    val isShowShelterPicker: Boolean = false,
    val shelterPickerStep: ShelterPickerStep = ShelterPickerStep.SIDO,
    val sidoList: ImmutableList<Sido> = persistentListOf(),
    val sigunguList: ImmutableList<Sigungu> = persistentListOf(),
    val shelters: ImmutableList<Shelter> = persistentListOf(),
    val selectedSido: Sido? = null,
    val selectedSigungu: Sigungu? = null,
    val shelterPickerQuery: String = "",
    val isLoadingPickerItems: Boolean = false,
    val shelterDetail: ShelterDetail? = null,
    val isLoadingShelterDetail: Boolean = false
) {
    val activeFilterCount: Int
        get() = listOf(filterUpkind, filterNeuter, filterState, filterCareRegNo).count { it.isNotEmpty() }
    val hasFilter: Boolean get() = activeFilterCount > 0
    val filteredShelters: ImmutableList<Shelter>
        get() = if (shelterPickerQuery.isEmpty()) shelters
                else shelters.filter { it.careNm.contains(shelterPickerQuery) }.toImmutableList()
}
