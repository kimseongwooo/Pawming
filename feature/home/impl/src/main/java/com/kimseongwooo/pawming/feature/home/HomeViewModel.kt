package com.kimseongwooo.pawming.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimseongwooo.pawming.domain.usecase.AddFavoriteUseCase
import com.kimseongwooo.pawming.domain.usecase.GetAbandonmentPublicUseCase
import com.kimseongwooo.pawming.domain.usecase.GetFavoriteIdsUseCase
import com.kimseongwooo.pawming.domain.usecase.GetShelterDetailUseCase
import com.kimseongwooo.pawming.domain.usecase.GetSheltersUseCase
import com.kimseongwooo.pawming.domain.usecase.GetSidoUseCase
import com.kimseongwooo.pawming.domain.usecase.GetSigunguUseCase
import com.kimseongwooo.pawming.domain.usecase.RemoveFavoriteUseCase
import com.kimseongwooo.pawming.model.Animal
import com.kimseongwooo.pawming.model.FavoriteAnimal
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PAGE_SIZE = 20

// ── State ──────────────────────────────────────────────────────────────────

// ── Intent ─────────────────────────────────────────────────────────────────

sealed interface HomeIntent {
    data object LoadInitial : HomeIntent
    data object LoadMore : HomeIntent
    data object ToggleFilter : HomeIntent
    data class SelectUpkind(val code: String) : HomeIntent
    data class SelectNeuter(val value: String) : HomeIntent
    data class SelectState(val value: String) : HomeIntent
    data object ShowShelterPicker : HomeIntent
    data object HideShelterPicker : HomeIntent
    data class UpdateShelterQuery(val query: String) : HomeIntent
    data class SelectShelter(val careRegNo: String, val careNm: String) : HomeIntent
    data object ClearShelter : HomeIntent
    data object ResetFilters : HomeIntent
    data class ClickAnimal(val desertionNo: String) : HomeIntent
    data class SelectSido(val sido: Sido) : HomeIntent
    data class SelectSigungu(val sigungu: Sigungu) : HomeIntent
    data object ShelterPickerBack : HomeIntent
    data class ViewShelterDetail(val careRegNo: String) : HomeIntent
    data object DismissShelterDetail : HomeIntent
    data class ToggleFavorite(val animal: Animal) : HomeIntent
    data class ApplyExternalShelterFilter(val careRegNo: String, val careNm: String) : HomeIntent
}

// ── SideEffect ─────────────────────────────────────────────────────────────

sealed interface HomeSideEffect {
    data class NavigateToAnimalDetail(val desertionNo: String) : HomeSideEffect
}

// ── ViewModel ──────────────────────────────────────────────────────────────

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAbandonmentPublicUseCase: GetAbandonmentPublicUseCase,
    private val getSheltersUseCase: GetSheltersUseCase,
    private val getShelterDetailUseCase: GetShelterDetailUseCase,
    private val getSidoUseCase: GetSidoUseCase,
    private val getSigunguUseCase: GetSigunguUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<HomeSideEffect> = _sideEffect.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        loadInitial()
        collectFavoriteIds()
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadInitial -> loadInitial()
            HomeIntent.LoadMore -> loadMore()
            HomeIntent.ToggleFilter -> _uiState.update { it.copy(isFilterOpen = !it.isFilterOpen) }
            is HomeIntent.SelectUpkind -> applyFilter { s ->
                s.copy(filterUpkind = if (s.filterUpkind == intent.code) "" else intent.code)
            }

            is HomeIntent.SelectNeuter -> applyFilter { s ->
                s.copy(filterNeuter = if (s.filterNeuter == intent.value) "" else intent.value)
            }

            is HomeIntent.SelectState -> applyFilter { s ->
                s.copy(filterState = if (s.filterState == intent.value) "" else intent.value)
            }

            HomeIntent.ShowShelterPicker -> {
                _uiState.update {
                    it.copy(
                        isShowShelterPicker = true,
                        shelterPickerStep = ShelterPickerStep.SIDO,
                        selectedSido = null,
                        selectedSigungu = null,
                        sigunguList = persistentListOf(),
                        shelters = persistentListOf(),
                        shelterPickerQuery = ""
                    )
                }
                loadSido()
            }

            HomeIntent.HideShelterPicker -> _uiState.update {
                it.copy(isShowShelterPicker = false, shelterPickerQuery = "")
            }

            is HomeIntent.UpdateShelterQuery -> _uiState.update { it.copy(shelterPickerQuery = intent.query) }
            is HomeIntent.SelectShelter -> applyFilter { s ->
                s.copy(
                    filterCareRegNo = intent.careRegNo,
                    filterCareNm = intent.careNm,
                    isShowShelterPicker = false,
                    shelterPickerQuery = ""
                )
            }

            HomeIntent.ClearShelter -> applyFilter { s ->
                s.copy(
                    filterCareRegNo = "",
                    filterCareNm = ""
                )
            }

            HomeIntent.ResetFilters -> applyFilter { s ->
                s.copy(
                    filterUpkind = "",
                    filterNeuter = "",
                    filterState = "",
                    filterCareRegNo = "",
                    filterCareNm = ""
                )
            }

            is HomeIntent.ClickAnimal -> viewModelScope.launch {
                _sideEffect.send(HomeSideEffect.NavigateToAnimalDetail(intent.desertionNo))
            }

            is HomeIntent.SelectSido -> {
                _uiState.update {
                    it.copy(
                        selectedSido = intent.sido,
                        shelterPickerStep = ShelterPickerStep.SIGUNGU,
                        sigunguList = persistentListOf(),
                        shelterPickerQuery = ""
                    )
                }
                loadSigungu(intent.sido.orgCd)
            }

            is HomeIntent.SelectSigungu -> {
                _uiState.update {
                    it.copy(
                        selectedSigungu = intent.sigungu,
                        shelterPickerStep = ShelterPickerStep.SHELTER,
                        shelters = persistentListOf(),
                        shelterPickerQuery = ""
                    )
                }
                loadShelters(intent.sigungu.uprCd, intent.sigungu.orgCd)
            }

            HomeIntent.ShelterPickerBack -> {
                val s = _uiState.value
                _uiState.update {
                    when (s.shelterPickerStep) {
                        ShelterPickerStep.SIGUNGU -> it.copy(
                            shelterPickerStep = ShelterPickerStep.SIDO,
                            selectedSido = null,
                            sigunguList = persistentListOf(),
                            shelterPickerQuery = ""
                        )

                        ShelterPickerStep.SHELTER -> if (s.selectedSigungu == null) {
                            it.copy(
                                shelterPickerStep = ShelterPickerStep.SIDO,
                                selectedSido = null,
                                shelters = persistentListOf(),
                                shelterPickerQuery = ""
                            )
                        } else {
                            it.copy(
                                shelterPickerStep = ShelterPickerStep.SIGUNGU,
                                selectedSigungu = null,
                                shelters = persistentListOf(),
                                shelterPickerQuery = ""
                            )
                        }

                        ShelterPickerStep.SIDO -> it
                    }
                }
            }

            is HomeIntent.ViewShelterDetail -> {
                _uiState.update { it.copy(isShowShelterPicker = false) }
                loadShelterDetail(intent.careRegNo)
            }

            HomeIntent.DismissShelterDetail -> _uiState.update {
                it.copy(shelterDetail = null, isLoadingShelterDetail = false)
            }

            is HomeIntent.ToggleFavorite -> toggleFavorite(intent.animal)
            is HomeIntent.ApplyExternalShelterFilter -> applyFilter { s ->
                s.copy(
                    filterCareRegNo = intent.careRegNo,
                    filterCareNm = intent.careNm,
                    isFilterOpen = true,
                    isShowShelterPicker = false
                )
            }
        }
    }

    private fun applyFilter(transform: (HomeUiState) -> HomeUiState) {
        _uiState.update(transform)
        loadInitial()
    }

    private fun loadInitial() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val s = _uiState.value
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    animals = persistentListOf(),
                    currentPage = 1,
                    hasMore = true
                )
            }
            getAbandonmentPublicUseCase(
                upkind = s.filterUpkind.ifEmpty { null },
                careRegNo = s.filterCareRegNo.ifEmpty { null },
                state = s.filterState.ifEmpty { null },
                neuterYn = s.filterNeuter.ifEmpty { null },
                pageNo = 1,
                numOfRows = PAGE_SIZE
            ).fold(
                onSuccess = { animals ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            animals = animals.toImmutableList(),
                            currentPage = 1,
                            hasMore = animals.size >= PAGE_SIZE
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "오류가 발생했습니다"
                        )
                    }
                }
            )
        }
    }

    private fun loadMore() {
        val s = _uiState.value
        if (!s.hasMore || s.isLoadingMore || s.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            val nextPage = s.currentPage + 1
            getAbandonmentPublicUseCase(
                upkind = s.filterUpkind.ifEmpty { null },
                careRegNo = s.filterCareRegNo.ifEmpty { null },
                state = s.filterState.ifEmpty { null },
                neuterYn = s.filterNeuter.ifEmpty { null },
                pageNo = nextPage,
                numOfRows = PAGE_SIZE
            ).fold(
                onSuccess = { newAnimals ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            animals = (it.animals + newAnimals).toImmutableList(),
                            currentPage = nextPage,
                            hasMore = newAnimals.size >= PAGE_SIZE
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoadingMore = false) }
                }
            )
        }
    }

    private fun loadSido() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPickerItems = true) }
            getSidoUseCase().fold(
                onSuccess = { sido ->
                    _uiState.update {
                        it.copy(
                            isLoadingPickerItems = false,
                            sidoList = sido.toImmutableList()
                        )
                    }
                },
                onFailure = { _uiState.update { it.copy(isLoadingPickerItems = false) } }
            )
        }
    }

    private fun loadSigungu(uprCd: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPickerItems = true) }
            getSigunguUseCase(uprCd).fold(
                onSuccess = { sigungu ->
                    if (sigungu.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                isLoadingPickerItems = false,
                                shelterPickerStep = ShelterPickerStep.SHELTER,
                                shelters = persistentListOf()
                            )
                        }
                        loadShelters(uprCd = uprCd, orgCd = uprCd)
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoadingPickerItems = false,
                                sigunguList = sigungu.toImmutableList()
                            )
                        }
                    }
                },
                onFailure = {
                    _uiState.update {
                        it.copy(isLoadingPickerItems = false)
                    }
                }
            )
        }
    }

    private fun loadShelters(uprCd: String, orgCd: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPickerItems = true) }
            getSheltersUseCase(uprCd = uprCd, orgCd = orgCd).fold(
                onSuccess = { shelters ->
                    _uiState.update {
                        it.copy(
                            isLoadingPickerItems = false,
                            shelters = shelters.toImmutableList()
                        )
                    }
                },
                onFailure = { _uiState.update { it.copy(isLoadingPickerItems = false) } }
            )
        }
    }

    private fun collectFavoriteIds() {
        getFavoriteIdsUseCase()
            .onEach { ids -> _uiState.update { it.copy(favoriteIds = ids.toImmutableSet()) } }
            .launchIn(viewModelScope)
    }

    private fun toggleFavorite(animal: Animal) {
        viewModelScope.launch {
            val isFavorite = _uiState.value.favoriteIds.contains(animal.desertionNo)
            if (isFavorite) {
                removeFavoriteUseCase(animal.desertionNo)
            } else {
                addFavoriteUseCase(
                    FavoriteAnimal(
                        desertionNo = animal.desertionNo,
                        kindNm = animal.kindNm,
                        sexCd = animal.sexCd,
                        age = animal.age,
                        happenPlace = animal.happenPlace,
                        processState = animal.processState,
                        imageUrl = animal.images.firstOrNull() ?: "",
                        savedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun loadShelterDetail(careRegNo: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingShelterDetail = true, shelterDetail = null) }
            getShelterDetailUseCase(careRegNo).fold(
                onSuccess = { detail ->
                    _uiState.update {
                        it.copy(
                            isLoadingShelterDetail = false,
                            shelterDetail = detail
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoadingShelterDetail = false) }
                }
            )
        }
    }
}
