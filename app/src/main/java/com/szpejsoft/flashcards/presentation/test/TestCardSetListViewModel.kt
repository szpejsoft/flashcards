package com.szpejsoft.flashcards.presentation.test

import com.szpejsoft.flashcards.domain.model.CardSet
import kotlinx.coroutines.flow.StateFlow

interface TestCardSetListViewModel {
    data class UiState(val cardSets: List<CardSet>)

    val uiState: StateFlow<UiState>
}