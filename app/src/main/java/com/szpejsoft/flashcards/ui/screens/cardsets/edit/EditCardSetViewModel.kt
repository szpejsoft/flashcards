package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.flashcard.SaveFlashcardUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetUiState.Busy
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetUiState.Idle
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditCardSetViewModel.Factory::class)
class EditCardSetViewModel
@AssistedInject
constructor(
    @Assisted
    private val cardSetId: Long,
    private val observeCardSetUseCase: ObserveCardSetUseCase,
    private val saveFlashcardUseCase: SaveFlashcardUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(cardSetId: Long): EditCardSetViewModel
    }

    val uiState: StateFlow<EditCardSetUiState> get() = _uiState

    private val _uiState = MutableStateFlow<EditCardSetUiState>(Busy)

    init {
        viewModelScope.launch {
            observeCardSetUseCase(cardSetId)
                .map { Idle(it.cardSet.name, it.flashcards) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000L),
                    initialValue = Busy
                )
                .collect { _uiState.value = it }
        }
    }

    fun onAddFlashcard(obverse: String, reverse: String) {
        viewModelScope.launch {
            _uiState.update { Busy } //will be updated to idle from observeCardSetUseCase
            saveFlashcardUseCase(cardSetId, obverse, reverse)
        }
    }

}

sealed class EditCardSetUiState {
    data object Busy : EditCardSetUiState()
    data class Idle(val cardSetName: String, val flashCards: List<Flashcard>) : EditCardSetUiState()
    data class Error(val message: String?) : EditCardSetUiState()
}