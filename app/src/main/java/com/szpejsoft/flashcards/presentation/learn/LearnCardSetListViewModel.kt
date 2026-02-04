package com.szpejsoft.flashcards.presentation.learn

import com.szpejsoft.flashcards.domain.model.CardSet
import kotlinx.coroutines.flow.StateFlow

interface LearnCardSetListViewModel {
    data class UiState(val cardSets: List<CardSet>)

    val uiState: StateFlow<UiState>
}