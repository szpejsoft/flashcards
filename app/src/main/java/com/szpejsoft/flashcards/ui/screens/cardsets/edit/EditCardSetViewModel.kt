package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.flashcard.DeleteFlashcardUseCase
import com.szpejsoft.flashcards.domain.usecase.flashcard.SaveFlashcardUseCase
import com.szpejsoft.flashcards.domain.usecase.flashcard.UpdateFlashcardUseCase
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
import kotlinx.coroutines.launch


@HiltViewModel(assistedFactory = EditCardSetViewModel.Factory::class)
open class EditCardSetViewModel
@AssistedInject
constructor(
    @Assisted
    private val cardSetId: Long,
    private val deleteFlashcardUseCase: DeleteFlashcardUseCase,
    private val observeCardSetUseCase: ObserveCardSetUseCase,
    private val saveFlashcardUseCase: SaveFlashcardUseCase,
    private val updateFlashcardUseCase: UpdateFlashcardUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(cardSetId: Long): EditCardSetViewModel
    }

    open val uiState: StateFlow<EditCardSetUiState> get() = _uiState

    private val _uiState = MutableStateFlow<EditCardSetUiState>(Idle())

    init {
        viewModelScope.launch {
            observeCardSetUseCase(cardSetId)
                .map { Idle(it.cardSet.name, it.flashcards) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000L),
                    initialValue = Idle()
                )
                .collect { _uiState.value = it }
        }
    }

    open fun onAddFlashcard(obverse: String, reverse: String) {
        viewModelScope.launch {
            saveFlashcardUseCase(cardSetId, obverse, reverse)
        }
    }

    open fun onDeleteFlashcard(flashcardId: Long) {
        viewModelScope.launch {
            deleteFlashcardUseCase(flashcardId)
        }
    }

    open fun onUpdateFlashcard(flashcardId: Long, obverse: String, reverse: String) {
        viewModelScope.launch {
            updateFlashcardUseCase(flashcardId, obverse, reverse)
        }
    }

}

sealed class EditCardSetUiState {
    data class Idle(val cardSetName: String = "", val flashCards: List<Flashcard> = emptyList()) : EditCardSetUiState()
    data class Error(val message: String?) : EditCardSetUiState()
}