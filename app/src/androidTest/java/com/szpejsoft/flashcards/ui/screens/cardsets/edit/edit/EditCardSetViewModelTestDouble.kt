package com.szpejsoft.flashcards.ui.screens.cardsets.edit.edit

import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetViewModel
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetViewModel.UiState
import kotlinx.coroutines.flow.MutableStateFlow

class EditCardSetViewModelTestDouble : EditCardSetViewModel {
    override val uiState = MutableStateFlow(UiState())

    val addFlashcardCalls: List<Pair<String, String>> get() = _addFlashcardsCalls
    val deleteFlashcardsCalls: List<Long> get() = _deleteFlashcardsCalls
    val updateFlashcardCalls: List<Triple<Long, String, String>> get() = _updateFlashcardsCalls

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

    override fun onUpdateCardSetName(cardSetName: String) {
        TODO("Not yet implemented")
    }

    override fun onSaveClicked() {
        saveClickedCallsNumber++
    }

    fun setUiState(state: UiState) {
        uiState.value = state
    }

}
