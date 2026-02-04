package com.szpejsoft.flashcards.presentation.cardsets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.szpejsoft.flashcards.domain.usecase.cardset.DeleteCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class EditCardSetListViewModelImpl
@Inject
constructor(
    observeCardSetsUseCase: ObserveCardSetsUseCase,
    private val deleteCardSetUseCase: DeleteCardSetUseCase
) : ViewModel(), EditCardSetListViewModel {

    override val uiState: StateFlow<EditCardSetListViewModel.UiState> =
        observeCardSetsUseCase()
            .map { EditCardSetListViewModel.UiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = EditCardSetListViewModel.UiState(emptyList())
            )

    override fun onDeleteCardSetClicked(cardSetId: Long) {
        viewModelScope.launch {
            deleteCardSetUseCase(cardSetId)
        }
    }

}



