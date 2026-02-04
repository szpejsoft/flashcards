package com.szpejsoft.flashcards.presentation.test

import androidx.lifecycle.ViewModel
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import com.szpejsoft.flashcards.presentation.common.stateInUi
import com.szpejsoft.flashcards.presentation.test.TestCardSetListViewModel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

@HiltViewModel
open class TestCardSetListViewModelImpl
@Inject
constructor(
    observeCardSetsUseCase: ObserveCardSetsUseCase
) : ViewModel(), TestCardSetListViewModel {

    override val uiState: StateFlow<UiState> =
        observeCardSetsUseCase()
            .map { UiState(it) }
            .stateInUi(initialValue = UiState(emptyList()))

}
