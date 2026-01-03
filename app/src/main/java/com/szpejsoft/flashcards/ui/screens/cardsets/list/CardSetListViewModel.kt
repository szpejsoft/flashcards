package com.szpejsoft.flashcards.ui.screens.cardsets.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.usecase.cardset.DeleteCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CardSetListViewModel
@Inject
constructor(
    observeCardSetsUseCase: ObserveCardSetsUseCase,
    private val deleteCardSetUseCase: DeleteCardSetUseCase
) : ViewModel() {

    val cardSets: StateFlow<CardSetListUiState> = observeCardSetsUseCase()
        .map { CardSetListUiState.Idle(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CardSetListUiState.Idle(emptyList())
        )

    fun onDeleteCardSetClicked(cardSetId: Long) {
        viewModelScope.launch {
            deleteCardSetUseCase(cardSetId)
        }
    }

}

sealed class CardSetListUiState {
    data class Idle(val cardSets: List<CardSet>) : CardSetListUiState()
}