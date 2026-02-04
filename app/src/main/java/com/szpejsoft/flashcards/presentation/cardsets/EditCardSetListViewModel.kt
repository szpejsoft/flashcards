package com.szpejsoft.flashcards.presentation.cardsets

import com.szpejsoft.flashcards.domain.model.CardSet
import kotlinx.coroutines.flow.StateFlow

interface EditCardSetListViewModel {
    data class UiState(val cardSets: List<CardSet>)
    val uiState: StateFlow<UiState>
    fun onDeleteCardSetClicked(cardSetId: Long)
}