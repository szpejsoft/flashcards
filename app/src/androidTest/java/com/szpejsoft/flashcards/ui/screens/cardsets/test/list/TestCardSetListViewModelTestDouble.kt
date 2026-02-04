package com.szpejsoft.flashcards.ui.screens.cardsets.test.list

import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.presentation.test.TestCardSetListViewModel
import com.szpejsoft.flashcards.presentation.test.TestCardSetListViewModel.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TestCardSetListViewModelTestDouble : TestCardSetListViewModel {
    override val uiState = MutableStateFlow(UiState(emptyList()))

    fun setCardSets(cardSets: List<CardSet>) {
        uiState.update { UiState(cardSets) }
    }

}
