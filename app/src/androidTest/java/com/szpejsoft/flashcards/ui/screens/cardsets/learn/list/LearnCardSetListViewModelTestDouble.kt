package com.szpejsoft.flashcards.ui.screens.cardsets.learn.list

import com.szpejsoft.flashcards.domain.model.CardSet
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LearnCardSetListViewModelTestDouble : LearnCardSetListViewModel(
    observeCardSetsUseCase = mockk(relaxed = true),
) {
    override val uiState = MutableStateFlow(LearnCardSetListUiState(emptyList()))

    fun setCardSets(cardSets: List<CardSet>) {
        uiState.update { LearnCardSetListUiState(cardSets) }
    }

}
