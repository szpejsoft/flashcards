package com.szpejsoft.flashcards.ui.screens.cardsets.list

import com.szpejsoft.flashcards.domain.model.CardSet
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CardSetListViewModelTestDouble : CardSetListViewModel(
    observeCardSetsUseCase = mockk(relaxed = true),
    deleteCardSetUseCase = mockk(relaxed = true)
) {
    override val uiState = MutableStateFlow<CardSetListUiState>(CardSetListUiState.Idle(emptyList()))
    val deleteCardSetCalls: List<Long> get() = _deleteCardSetCalls

    private val _deleteCardSetCalls = mutableListOf<Long>()

    fun setCardSets(cardSets: List<CardSet>) {
        uiState.update { CardSetListUiState.Idle(cardSets) }
    }

    override fun onDeleteCardSetClicked(cardSetId: Long) {
        _deleteCardSetCalls.add(cardSetId)
    }

}
