package com.szpejsoft.flashcards.presentation.cardsets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.SaveCardSetUseCase
import com.szpejsoft.flashcards.presentation.cardsets.AddCardSetViewModel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AddCardSetViewModelImpl
@Inject
constructor(
    private val saveCardSetUseCase: SaveCardSetUseCase
) : ViewModel(), AddCardSetViewModel {

    override val uiState: StateFlow<UiState>
        get() = _uiState

    private val _uiState = MutableStateFlow(UiState())

    private var newFlashcardId = 1L

    override fun resetState() {
        _uiState.value = UiState()
        newFlashcardId = 1
    }

    override fun onCardSetNameChanged(name: String) {
        _uiState.update { state ->
            state.copy(setName = name, saveEnabled = isSaveEnabled(name, state.flashCards))
        }
    }

    override fun onAddFlashcard(obverse: String, reverse: String) {
        _uiState.update { state ->
            val newCard = Flashcard(newFlashcardId++, obverse, reverse)
            val newFlashcards = state.flashCards + newCard
            state.copy(
                flashCards = newFlashcards,
                saveEnabled = isSaveEnabled(state.setName, newFlashcards)
            )
        }
    }

    override fun onDeleteFlashcard(flashcardId: Long) {
        _uiState.update { state ->
            val newFlashcards = state.flashCards.filterNot { it.id == flashcardId }
            state.copy(
                flashCards = newFlashcards,
                saveEnabled = isSaveEnabled(state.setName, newFlashcards)
            )
        }
    }

    override fun onUpdateFlashcard(flashcardId: Long, obverse: String, reverse: String) {
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

    override fun onSaveChanges() {
        viewModelScope.launch {
            saveCardSetUseCase(
                cardSetName = _uiState.value.setName,
                flashcards = _uiState.value.flashCards
            )
        }
    }

    private fun isSaveEnabled(setName: String, flashCards: List<Flashcard>) =
        setName.isNotBlank() && flashCards.isNotEmpty()

}


