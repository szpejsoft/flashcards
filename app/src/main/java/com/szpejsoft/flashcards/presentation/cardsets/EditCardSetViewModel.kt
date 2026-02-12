package com.szpejsoft.flashcards.presentation.cardsets

import com.szpejsoft.flashcards.domain.model.Flashcard
import kotlinx.coroutines.flow.StateFlow

interface EditCardSetViewModel {

    interface Factory {
        fun create(cardSetId: Long): EditCardSetViewModel
    }

    data class UiState(
        val setName: String = "",
        val flashCards: List<Flashcard> = emptyList(),
        val saveEnabled: Boolean = false,
    )

    val uiState: StateFlow<UiState>

    fun onAddFlashcard(obverse: String, reverse: String)
    fun onDeleteFlashcard(flashcardId: Long)
    fun onUpdateFlashcard(flashcardId: Long, obverse: String, reverse: String)
    fun onUpdateCardSetName(cardSetName: String)
    fun onSaveChanges()

}