package com.kimseongwooo.pawming.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimseongwooo.pawming.domain.usecase.GetFavoritesUseCase
import com.kimseongwooo.pawming.domain.usecase.RemoveFavoriteUseCase
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
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<FavoritesSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<FavoritesSideEffect> = _sideEffect.receiveAsFlow()

    init {
        getFavoritesUseCase()
            .onEach { list ->
                _uiState.update {
                    it.copy(favorites = list.toImmutableList(), isLoading = false)
                }
            }
            .launchIn(viewModelScope)
    }

    fun handleIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.ClickAnimal -> viewModelScope.launch {
                _sideEffect.send(FavoritesSideEffect.NavigateToAnimalDetail(intent.desertionNo))
            }
            is FavoritesIntent.RemoveFavorite -> viewModelScope.launch {
                removeFavoriteUseCase(intent.desertionNo)
            }
        }
    }
}
