package com.szpejsoft.flashcards.ui.screens.cardsets.add

import com.szpejsoft.flashcards.ui.screens.cardsets.add.AddCardSetUiState.Editing
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

class AddCardSetViewModelTestDouble : AddCardSetViewModel(
    saveCardSetUseCase = mockk(relaxed = true)
) {
    override val uiState = MutableStateFlow<AddCardSetUiState>(Editing())

    var onSaveClickedCounter = 0
        private set
    val onCardSetNameChangedCalls: List<String> get() = _onCardSetNameChanged

    private val _onCardSetNameChanged = mutableListOf<String>()

    override fun resetState() {
        uiState.value = Editing()
        _onCardSetNameChanged.clear()
        onSaveClickedCounter = 0
    }

    override fun onCardSetNameChanged(name: String) {
        _onCardSetNameChanged.add(name)
        uiState.value = Editing(name, name.isNotBlank())
    }

    override fun onSaveClicked() {
        onSaveClickedCounter++
    }

    fun setUiState(state: AddCardSetUiState) {
        uiState.value = state
    }

}

