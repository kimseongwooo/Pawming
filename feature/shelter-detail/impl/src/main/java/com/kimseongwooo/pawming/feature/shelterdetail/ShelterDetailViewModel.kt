package com.kimseongwooo.pawming.feature.shelterdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimseongwooo.pawming.domain.usecase.GetShelterDetailUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ShelterDetailViewModel.Factory::class)
class ShelterDetailViewModel @AssistedInject constructor(
    @Assisted private val careRegNo: String,
    private val getShelterDetailUseCase: GetShelterDetailUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(careRegNo: String): ShelterDetailViewModel
    }

    private val _uiState = MutableStateFlow(ShelterDetailUiState())
    val uiState: StateFlow<ShelterDetailUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ShelterDetailSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ShelterDetailSideEffect> = _sideEffect.receiveAsFlow()

    init {
        load()
    }

    fun handleIntent(intent: ShelterDetailIntent) {
        when (intent) {
            ShelterDetailIntent.Retry -> load()
            ShelterDetailIntent.NavigateBack -> viewModelScope.launch {
                _sideEffect.send(ShelterDetailSideEffect.Back)
            }
            ShelterDetailIntent.CallPhone -> {
                val tel = _uiState.value.shelterDetail?.careTel ?: return
                viewModelScope.launch { _sideEffect.send(ShelterDetailSideEffect.Dial(tel)) }
            }
            ShelterDetailIntent.ViewAnimals -> {
                val detail = _uiState.value.shelterDetail ?: return
                viewModelScope.launch {
                    _sideEffect.send(
                        ShelterDetailSideEffect.NavigateToAnimalsWithShelterFilter(
                            careRegNo = detail.careRegNo,
                            careNm = detail.careNm
                        )
                    )
                }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getShelterDetailUseCase(careRegNo).fold(
                onSuccess = { detail ->
                    _uiState.update { it.copy(isLoading = false, shelterDetail = detail) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "오류가 발생했습니다") }
                }
            )
        }
    }
}
