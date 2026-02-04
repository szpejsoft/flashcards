package com.szpejsoft.flashcards.ui.screens.cardsets.edit.list

import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetListUiState
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetListViewModel
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class EditCardSetListViewModelTestDouble : EditCardSetListViewModel(
    observeCardSetsUseCase = mockk(relaxed = true),
    deleteCardSetUseCase = mockk(relaxed = true)
) {
    override val uiState = MutableStateFlow(EditCardSetListUiState(emptyList()))

    val deleteCardSetCalls: List<Long> get() = _deleteCardSetCalls

    private val _deleteCardSetCalls = mutableListOf<Long>()

    override fun onDeleteCardSetClicked(cardSetId: Long) {
        _deleteCardSetCalls.add(cardSetId)
    }

    fun setCardSets(cardSets: List<CardSet>) {
        uiState.update { EditCardSetListUiState(cardSets) }
    }

}
