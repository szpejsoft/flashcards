package com.szpejsoft.flashcards.presentation.learn

import androidx.lifecycle.ViewModel
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import com.szpejsoft.flashcards.presentation.common.stateInUi
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetListViewModel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

@HiltViewModel
class LearnCardSetListViewModelImpl
@Inject
constructor(
    observeCardSetsUseCase: ObserveCardSetsUseCase
) : ViewModel(), LearnCardSetListViewModel {

    override val uiState: StateFlow<UiState> =
        observeCardSetsUseCase()
            .map { UiState(it) }
            .stateInUi(initialValue = UiState(emptyList()))
}
