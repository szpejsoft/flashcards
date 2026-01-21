package com.szpejsoft.flashcards.ui.screens.cardsets.test.list

import com.szpejsoft.flashcards.domain.model.CardSet
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TestCardSetListViewModelTestDouble : TestCardSetListViewModel(
    observeCardSetsUseCase = mockk(relaxed = true),
) {
    override val uiState = MutableStateFlow(TestCardSetListUiState(emptyList()))

    val deleteCardSetCalls: List<Long> get() = _deleteCardSetCalls

    private val _deleteCardSetCalls = mutableListOf<Long>()


    fun setCardSets(cardSets: List<CardSet>) {
        uiState.update { TestCardSetListUiState(cardSets) }
    }

}
