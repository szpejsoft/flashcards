package com.szpejsoft.flashcards.ui.screens.cardsets.edit.add


import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.presentation.cardsets.AddCardSetViewModel
import com.szpejsoft.flashcards.presentation.cardsets.AddCardSetViewModel.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AddCardSetViewModelTestDouble : AddCardSetViewModel {
    override val uiState = MutableStateFlow(UiState())
    val onAddFlashcardCalls: List<Pair<String, String>> get() = _onAddFlashcardCalls
    val onCardSetNameChangedCalls: List<String> get() = _onCardSetNameChangedCalls
    val onDeleteFlashcardCalls: List<Long> get() = _onDeleteFlashcardCalls
    val onUpdateFlashcardCalls: List<Triple<Long, String, String>> get() = _onUpdateFlashcardCalls


    var onSaveClickedCounter = 0
        private set

    private val _onAddFlashcardCalls = mutableListOf<Pair<String, String>>()
    private val _onCardSetNameChangedCalls = mutableListOf<String>()
    private val _onDeleteFlashcardCalls = mutableListOf<Long>()
    private val _onUpdateFlashcardCalls = mutableListOf<Triple<Long, String, String>>()

    override fun resetState() {
        uiState.value = UiState()
        _onCardSetNameChangedCalls.clear()
        onSaveClickedCounter = 0
    }

    override fun onCardSetNameChanged(name: String) {
        _onCardSetNameChangedCalls.add(name)
        uiState.update { state ->
            state.copy(setName = name, saveEnabled = isSaveEnabled(name, state.flashCards))
        }
    }

    override fun onUpdateFlashcard(flashcardId: Long, obverse: String, reverse: String) {
        _onUpdateFlashcardCalls.add(Triple(flashcardId, obverse, reverse))
    }

    override fun onDeleteFlashcard(flashcardId: Long) {
        _onDeleteFlashcardCalls.add(flashcardId)
    }

    override fun onAddFlashcard(obverse: String, reverse: String) {
        _onAddFlashcardCalls.add(obverse to reverse)
    }

    override fun onSaveClicked() {
        onSaveClickedCounter++
    }

    fun setUiState(state: UiState) {
        uiState.value = state
    }

    private fun isSaveEnabled(setName: String, flashCards: List<Flashcard>) =
        setName.isNotBlank() && flashCards.isNotEmpty()
}
