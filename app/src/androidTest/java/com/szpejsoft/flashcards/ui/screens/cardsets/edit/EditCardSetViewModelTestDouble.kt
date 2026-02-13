package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetViewModel
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetViewModel.UiState
import kotlinx.coroutines.flow.MutableStateFlow

class EditCardSetViewModelTestDouble : EditCardSetViewModel {
    override val uiState = MutableStateFlow(UiState())

    val addFlashcardCalls: List<Pair<String, String>> get() = _addFlashcardsCalls
    val deleteFlashcardsCalls: List<Long> get() = _deleteFlashcardsCalls
    val updateFlashcardCalls: List<Triple<Long, String, String>> get() = _updateFlashcardsCalls
    val updateCardSetNameCalls: List<String> get() = _updateCardSetNameCalls

    var saveClickedCallsNumber = 0
        private set

    private val _addFlashcardsCalls = mutableListOf<Pair<String, String>>()
    private val _deleteFlashcardsCalls = mutableListOf<Long>()
    private val _updateFlashcardsCalls = mutableListOf<Triple<Long, String, String>>()
    private val _updateCardSetNameCalls = mutableListOf<String>()

    override fun onAddFlashcard(obverse: String, reverse: String) {
        _addFlashcardsCalls.add(obverse to reverse)
    }

    override fun onDeleteFlashcard(flashcardId: Long) {
        _deleteFlashcardsCalls.add(flashcardId)
    }

    override fun onUpdateCardSetName(cardSetName: String) {
        _updateCardSetNameCalls.add(cardSetName)
    }

    override fun onUpdateFlashcard(flashcardId: Long, obverse: String, reverse: String) {
        _updateFlashcardsCalls.add(Triple(flashcardId, obverse, reverse))
    }

    override fun onSaveChanges() {
        saveClickedCallsNumber++
    }

    fun setUiState(state: UiState) {
        uiState.value = state
    }

}
