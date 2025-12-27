package com.szpejsoft.flashcards.ui.screens.cardsets.list

import com.szpejsoft.flashcards.domain.model.CardSet
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CardSetListViewModel {
    val cardSets: StateFlow<List<CardSet>> get() = _cardSets
    val deleteCardSetCalls: List<Long> get() = _deleteCardSetCalls

    private val _cardSets = MutableStateFlow<List<CardSet>>(emptyList())
    private val _deleteCardSetCalls = mutableListOf<Long>()

    fun setCardSets(cardSets: List<CardSet>) {
        _cardSets.update { cardSets }
    }

    fun onDeleteCardSetClicked(cardSetId: Long) {
        _deleteCardSetCalls.add(cardSetId)
        _cardSets.update { sets -> sets.filterNot { it.id == cardSetId } }
    }

}