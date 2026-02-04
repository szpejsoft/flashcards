package com.szpejsoft.flashcards.ui.screens.cardsets.learn.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
open class LearnCardSetListViewModel
@Inject
constructor(
    observeCardSetsUseCase: ObserveCardSetsUseCase
) : ViewModel() {

    open val uiState: StateFlow<LearnCardSetListUiState> =
        observeCardSetsUseCase()
            .map { LearnCardSetListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = LearnCardSetListUiState(emptyList())
            )
}

data class LearnCardSetListUiState(val cardSets: List<CardSet>)

