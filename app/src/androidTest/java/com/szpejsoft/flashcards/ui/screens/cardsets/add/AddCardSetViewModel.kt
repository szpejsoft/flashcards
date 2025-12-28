package com.szpejsoft.flashcards.ui.screens.cardsets.add

import androidx.lifecycle.ViewModel
import com.szpejsoft.flashcards.ui.screens.cardsets.add.AddCardSetUiState.Editing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddCardSetViewModel : ViewModel() {
    val uiState: StateFlow<AddCardSetUiState>
        get() = _uiState.asStateFlow()

    var onSaveClickedCounter = 0
        private set
    val onCardSetNameChangedCalls: List<String> get() = _onCardSetNameChanged

    private val _uiState = MutableStateFlow<AddCardSetUiState>(Editing())
    private val _onCardSetNameChanged = mutableListOf<String>()

    fun resetState() {
        _uiState.update { Editing() }
        _onCardSetNameChanged.clear()
        onSaveClickedCounter = 0
    }

    fun setUiState(state: AddCardSetUiState) {
        _uiState.update { state }
    }

    fun onCardSetNameChanged(name: String) {
        _onCardSetNameChanged.add(name)
        _uiState.update { Editing(name, name.isNotBlank()) }
    }

    fun onSaveClicked() {
        onSaveClickedCounter++
    }

}

sealed class AddCardSetUiState {
    data class Editing(val name: String = "", val isSaveEnabled: Boolean = false) : AddCardSetUiState()
    data class Error(val message: String) : AddCardSetUiState()
    data object Saving : AddCardSetUiState()
    data object Saved : AddCardSetUiState()
}