package com.kimseongwooo.pawming.feature.shelter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimseongwooo.pawming.domain.usecase.GetSheltersUseCase
import com.kimseongwooo.pawming.domain.usecase.GetSidoUseCase
import com.kimseongwooo.pawming.domain.usecase.GetSigunguUseCase
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShelterViewModel @Inject constructor(
    private val getSidoUseCase: GetSidoUseCase,
    private val getSigunguUseCase: GetSigunguUseCase,
    private val getSheltersUseCase: GetSheltersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShelterUiState())
    val uiState: StateFlow<ShelterUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ShelterSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    // 시도별 시군구 목록을 미리 보관 — 선택 시 즉시 조회
    private var sigunguCache: Map<String, ImmutableList<Sigungu>> = emptyMap()

    init {
        loadInitialData()
    }

    fun handleIntent(intent: ShelterIntent) {
        when (intent) {
            is ShelterIntent.SelectSido -> onSidoSelected(intent.sido)
            is ShelterIntent.SelectSigungu -> onSigunguSelected(intent.sigungu)
            is ShelterIntent.UpdateSearchQuery -> _uiState.update { it.copy(searchQuery = intent.query) }
            is ShelterIntent.ClickShelter -> viewModelScope.launch {
                _sideEffect.send(ShelterSideEffect.NavigateToShelterDetail(intent.careRegNo))
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            getSidoUseCase().onSuccess { sidoList ->
                _uiState.update { it.copy(sidoList = sidoList.toImmutableList()) }

                // 시도별 시군구 전체 병렬 프리패치
                val cache = coroutineScope {
                    sidoList.map { sido ->
                        async {
                            sido.orgCd to (getSigunguUseCase(sido.orgCd).getOrDefault(emptyList()).toImmutableList())
                        }
                    }.awaitAll().toMap()
                }
                sigunguCache = cache
            }
            _uiState.update { it.copy(isInitialLoading = false) }
        }
    }

    private fun onSidoSelected(sido: Sido) {
        val sigunguList = sigunguCache[sido.orgCd] ?: persistentListOf()

        if (sigunguList.isEmpty()) {
            // 세종특별자치시 등 별도 시/군/구가 없는 경우 — 시도 코드로 직접 조회
            val syntheticSigungu = Sigungu(
                orgCd = sido.orgCd,
                orgdownNm = sido.orgdownNm,
                uprCd = sido.orgCd
            )
            _uiState.update {
                it.copy(
                    selectedSido = sido,
                    sigunguList = persistentListOf(),
                    selectedSigungu = syntheticSigungu,
                    shelters = persistentListOf(),
                    searchQuery = ""
                )
            }
            loadShelters(uprCd = sido.orgCd, orgCd = sido.orgCd)
        } else {
            _uiState.update {
                it.copy(
                    selectedSido = sido,
                    sigunguList = sigunguList,
                    selectedSigungu = null,
                    shelters = persistentListOf(),
                    searchQuery = ""
                )
            }
        }
    }

    private fun onSigunguSelected(sigungu: Sigungu) {
        _uiState.update { it.copy(selectedSigungu = sigungu, searchQuery = "") }
        loadShelters(uprCd = sigungu.uprCd, orgCd = sigungu.orgCd)
    }

    private fun loadShelters(uprCd: String, orgCd: String) {
        _uiState.update { it.copy(isShelterLoading = true, error = null) }
        viewModelScope.launch {
            getSheltersUseCase(uprCd = uprCd, orgCd = orgCd)
                .onSuccess { list ->
                    _uiState.update { it.copy(shelters = list.toImmutableList(), isShelterLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isShelterLoading = false, error = e.message) }
                }
        }
    }
}
