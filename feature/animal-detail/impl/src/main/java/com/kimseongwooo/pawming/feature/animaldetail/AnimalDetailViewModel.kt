package com.kimseongwooo.pawming.feature.animaldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimseongwooo.pawming.domain.usecase.AddFavoriteUseCase
import com.kimseongwooo.pawming.domain.usecase.GetAnimalByDesertionNoUseCase
import com.kimseongwooo.pawming.domain.usecase.GetFavoriteIdsUseCase
import com.kimseongwooo.pawming.domain.usecase.RemoveFavoriteUseCase
import com.kimseongwooo.pawming.model.FavoriteAnimal
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AnimalDetailViewModel.Factory::class)
class AnimalDetailViewModel @AssistedInject constructor(
    @Assisted private val desertionNo: String,
    private val getAnimalByDesertionNoUseCase: GetAnimalByDesertionNoUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(desertionNo: String): AnimalDetailViewModel
    }

    private val _uiState = MutableStateFlow(AnimalDetailUiState())
    val uiState: StateFlow<AnimalDetailUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<AnimalDetailSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<AnimalDetailSideEffect> = _sideEffect.receiveAsFlow()

    init {
        loadAnimal()
        collectFavoriteIds()
    }

    fun handleIntent(intent: AnimalDetailIntent) {
        when (intent) {
            AnimalDetailIntent.ToggleFavorite -> toggleFavorite()
            AnimalDetailIntent.Retry -> loadAnimal()
        }
    }

    private fun loadAnimal() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getAnimalByDesertionNoUseCase(desertionNo).fold(
                onSuccess = { animal ->
                    _uiState.update { it.copy(isLoading = false, animal = animal) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "오류가 발생했습니다") }
                }
            )
        }
    }

    private fun collectFavoriteIds() {
        getFavoriteIdsUseCase()
            .onEach { ids -> _uiState.update { it.copy(isFavorite = ids.contains(desertionNo)) } }
            .launchIn(viewModelScope)
    }

    private fun toggleFavorite() {
        val state = _uiState.value
        val animal = state.animal ?: return
        viewModelScope.launch {
            if (state.isFavorite) {
                removeFavoriteUseCase(desertionNo)
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
}
