package com.szpejsoft.flashcards.ui.screens.cardsets.edit

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
                        isModified = false,
                        isSaving = false
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
            state.copy(
                flashCards = state.flashCards + newCard,
                isModified = true
            )
        }
    }

    open fun onDeleteFlashcard(flashcardId: Long) {
        deletedFlashcardIds.add(flashcardId)
        _uiState.update { state ->
            state.copy(
                flashCards = state.flashCards.filterNot { it.id == flashcardId },
                isModified = true
            )
        }
    }

    open fun onUpdateFlashcard(flashcardId: Long, obverse: String, reverse: String) {
        _uiState.update { state ->
            val updatedCard = state.flashCards.first { it.id == flashcardId }.copy(
                obverse = obverse,
                reverse = reverse
            )
            state.copy(
                flashCards = state.flashCards.filterNot { it.id == flashcardId } + updatedCard,
                isModified = true
            )

        }
    }

    open fun onUpdateCardSetName(cardSetName: String) {
        _uiState.update { state ->
            state.copy(setName = cardSetName, isModified = true)
        }
    }

    open fun onSaveClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                updateCardSetUseCase(
                    cardSetId = cardSetId,
                    cardSetName = _uiState.value.setName,
                    flashcardsToSave = _uiState.value.flashCards,
                    flashcardIdsToDelete = deletedFlashcardIds.toList()
                )
            } catch (_: Exception) {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

}

data class EditCardSetUiState(
    val setName: String = "",
    val flashCards: List<Flashcard> = emptyList(),
    val isModified: Boolean = false,
    val isSaving: Boolean = false
)
