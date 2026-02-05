package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetListViewModel
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetListViewModel.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class EditCardSetListViewModelTestDouble : EditCardSetListViewModel {
    override val uiState = MutableStateFlow(UiState(emptyList()))

    val deleteCardSetCalls: List<Long> get() = _deleteCardSetCalls

    private val _deleteCardSetCalls = mutableListOf<Long>()

    override fun onDeleteCardSetClicked(cardSetId: Long) {
        _deleteCardSetCalls.add(cardSetId)
    }

    fun setCardSets(cardSets: List<CardSet>) {
        uiState.update { UiState(cardSets) }
    }

}
