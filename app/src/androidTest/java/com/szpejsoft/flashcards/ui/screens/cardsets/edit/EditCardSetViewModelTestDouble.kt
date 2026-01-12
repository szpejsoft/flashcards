package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

class EditCardSetViewModelTestDouble : EditCardSetViewModel(
    cardSetId = 1L,
    observeCardSetUseCase = mockk(relaxed = true),
    updateCardSetUseCase = mockk(relaxed = true),
) {
    override val uiState = MutableStateFlow(EditCardSetUiState())

    val addFlashcardCalls: List<Pair<String, String>> get() = _addFlashcardsCalls
    val deleteFlashcardsCalls: List<Long> get() = _deleteFlashcardsCalls
    val updateFlashcardsCalls: List<Triple<Long, String, String>> get() = _updateFlashcardsCalls

    var saveClickedCallsNumber = 0
        private set

    private val _addFlashcardsCalls = mutableListOf<Pair<String, String>>()
    private val _deleteFlashcardsCalls = mutableListOf<Long>()
    private val _updateFlashcardsCalls = mutableListOf<Triple<Long, String, String>>()

    override fun onAddFlashcard(obverse: String, reverse: String) {
        _addFlashcardsCalls.add(obverse to reverse)
    }

    override fun onDeleteFlashcard(flashcardId: Long) {
        _deleteFlashcardsCalls.add(flashcardId)
    }

    override fun onUpdateFlashcard(flashcardId: Long, obverse: String, reverse: String) {
        _updateFlashcardsCalls.add(Triple(flashcardId, obverse, reverse))
    }

    override fun onSaveClicked() {
        saveClickedCallsNumber++
    }

    fun setUiState(state: EditCardSetUiState) {
        uiState.value = state
    }
}


