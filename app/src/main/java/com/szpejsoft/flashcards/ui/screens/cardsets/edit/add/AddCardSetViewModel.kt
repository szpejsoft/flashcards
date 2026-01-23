package com.szpejsoft.flashcards.ui.screens.cardsets.edit.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.SaveCardSetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
open class AddCardSetViewModel
@Inject
constructor(
    private val saveCardSetUseCase: SaveCardSetUseCase
) : ViewModel() {

    open val uiState: StateFlow<AddCardSetUiState>
        get() = _uiState

    private val _uiState = MutableStateFlow(AddCardSetUiState())

    private var newFlashcardId = 1L

    open fun resetState() {
        _uiState.value = AddCardSetUiState()
        newFlashcardId = 1
    }

    open fun onCardSetNameChanged(name: String) {
        _uiState.update { state ->
            state.copy(setName = name, saveEnabled = isSaveEnabled(name, state.flashCards))
        }
    }

    open fun onAddFlashcard(obverse: String, reverse: String) {
        _uiState.update { state ->
            val newCard = Flashcard(newFlashcardId++, obverse, reverse)
            val newFlashcards = state.flashCards + newCard
            state.copy(
                flashCards = newFlashcards,
                saveEnabled = isSaveEnabled(state.setName, newFlashcards)
            )
        }
    }

    open fun onDeleteFlashcard(flashcardId: Long) {
        _uiState.update { state ->
            val newFlashcards = state.flashCards.filterNot { it.id == flashcardId }
            state.copy(
                flashCards = newFlashcards,
                saveEnabled = isSaveEnabled(state.setName, newFlashcards)
            )
        }
    }

    open fun onUpdateFlashcard(flashcardId: Long, obverse: String, reverse: String) {
        _uiState.update { state ->
            val updatedCard = state.flashCards.first { it.id == flashcardId }.copy(
                obverse = obverse,
                reverse = reverse
            )
            val newFlashcards = state.flashCards.filterNot { it.id == flashcardId } + updatedCard
            state.copy(
                flashCards = newFlashcards,
                saveEnabled = newFlashcards.isNotEmpty()
            )
        }
    }

    open fun onSaveClicked() {
        viewModelScope.launch {
            saveCardSetUseCase(
                cardSetName = _uiState.value.setName,
                flashcards = _uiState.value.flashCards
            )
        }
    }

    protected fun isSaveEnabled(setName: String, flashCards: List<Flashcard>) =
        setName.isNotBlank() && flashCards.isNotEmpty()

}

data class AddCardSetUiState(
    val setName: String = "",
    val flashCards: List<Flashcard> = emptyList(),
    val saveEnabled: Boolean = false,
)
