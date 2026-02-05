package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetListViewModel
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetListViewModel.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LearnCardSetListViewModelTestDouble : LearnCardSetListViewModel {
    override val uiState = MutableStateFlow(UiState(emptyList()))

    fun setCardSets(cardSets: List<CardSet>) {
        uiState.update { UiState(cardSets) }
    }

}
