package com.szpejsoft.flashcards.ui.screens.cardsets.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.usecase.DeleteCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.ObserveCardSetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CardSetListViewModel
@Inject constructor(
    private val observeCardSetsUseCase: ObserveCardSetsUseCase,
    private val deleteCardSetUseCase: DeleteCardSetUseCase
) : ViewModel() {

    val cardSets: StateFlow<List<CardSet>> = observeCardSetsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun onDeleteCardSetClicked(cardSetId: Long) {
        viewModelScope.launch {
            deleteCardSetUseCase(cardSetId)
        }
    }

}