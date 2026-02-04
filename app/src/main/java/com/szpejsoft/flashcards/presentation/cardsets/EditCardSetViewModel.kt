package com.szpejsoft.flashcards.presentation.cardsets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.cardset.UpdateCardSetUseCase
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
open class EditCardSetViewModel
@AssistedInject
constructor(
    @Assisted
    private val cardSetId: Long,
    private val observeCardSetUseCase: ObserveCardSetUseCase,
    private val updateCardSetUseCase: UpdateCardSetUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(cardSetId: Long): EditCardSetViewModel
    }

    open val uiState: StateFlow<EditCardSetUiState> get() = _uiState

    private val _uiState = MutableStateFlow(EditCardSetUiState())
    private val deletedFlashcardIds = mutableSetOf<Long>()

    init {
        viewModelScope.launch {
            observeCardSetUseCase(cardSetId)
                .map {
                    EditCardSetUiState(
                        setName = it.cardSet.name,
                        flashCards = it.flashcards,
                        saveEnabled = false,
                    )
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000L),
                    initialValue = EditCardSetUiState()
                )
                .collect { _uiState.value = it }
        }
    }

    open fun onAddFlashcard(obverse: String, reverse: String) {
        _uiState.update { state ->
            val newCard = Flashcard(0, obverse, reverse)
            val newFlashcards = state.flashCards + newCard
            state.copy(
                flashCards = newFlashcards,
                saveEnabled = newFlashcards.isNotEmpty()
            )
        }
    }

    open fun onDeleteFlashcard(flashcardId: Long) {
        deletedFlashcardIds.add(flashcardId)
        _uiState.update { state ->
            val newFlashcards = state.flashCards.filterNot { it.id == flashcardId }
            state.copy(
                flashCards = newFlashcards,
                saveEnabled = newFlashcards.isNotEmpty()
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

    open fun onUpdateCardSetName(cardSetName: String) {
        _uiState.update { state ->
            state.copy(setName = cardSetName, saveEnabled = state.flashCards.isNotEmpty())
        }
    }

    open fun onSaveClicked() {
        viewModelScope.launch {
            updateCardSetUseCase(
                cardSetId = cardSetId,
                cardSetName = _uiState.value.setName,
                flashcardsToSave = _uiState.value.flashCards,
                flashcardIdsToDelete = deletedFlashcardIds.toList()
            )
            _uiState.update { it.copy(saveEnabled = false) }
        }
    }

}

data class EditCardSetUiState(
    val setName: String = "",
    val flashCards: List<Flashcard> = emptyList(),
    val saveEnabled: Boolean = false,
)
