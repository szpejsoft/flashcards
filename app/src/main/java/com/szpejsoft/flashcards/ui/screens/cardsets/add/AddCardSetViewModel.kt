package com.szpejsoft.flashcards.ui.screens.cardsets.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.usecase.cardset.SaveCardSetUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.add.AddCardSetUiState.Editing
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AddCardSetViewModel
@Inject
constructor(
    private val saveCardSetUseCase: SaveCardSetUseCase
) : ViewModel() {

    val uiState: StateFlow<AddCardSetUiState>
        get() = _uiState

    private val _uiState = MutableStateFlow<AddCardSetUiState>(Editing())

    fun resetState() {
        _uiState.value = Editing()
    }

    fun onCardSetNameChanged(name: String) {
        if (_uiState.value is Editing) {
            _uiState.update { Editing(name, name.isNotBlank()) }
        }
    }

    fun onSaveClicked() {
        val currentState = _uiState.value
        if (currentState is Editing && currentState.isSaveEnabled) {
            viewModelScope.launch {
                _uiState.value = AddCardSetUiState.Saving
                try {
                    saveCardSetUseCase(currentState.name)
                    _uiState.value = AddCardSetUiState.Saved
                } catch (e: Exception) {
                    _uiState.value = AddCardSetUiState.Error(e.message)
                }
            }
        }
    }

}

sealed class AddCardSetUiState {
    data class Editing(val name: String = "", val isSaveEnabled: Boolean = false) : AddCardSetUiState()
    data class Error(val message: String?) : AddCardSetUiState()
    data object Saving : AddCardSetUiState()
    data object Saved : AddCardSetUiState()
}