package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.ui.log
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditCardSetViewModel.Factory::class)
class EditCardSetViewModel
@AssistedInject
constructor(
    @Assisted
    private val cardSetId: Long,
    private val observeCardSetUseCase: ObserveCardSetUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(cardSetId: Long): EditCardSetViewModel
    }

    val uiState: StateFlow<EditCardSetUiState> get() = _uiState

    private val _uiState = MutableStateFlow<EditCardSetUiState>(EditCardSetUiState.Loading)

    init {
        log("ECSVM created with setId=$cardSetId")
        viewModelScope.launch {
            observeCardSetUseCase(cardSetId)
                .map { EditCardSetUiState.Idle(it.cardSet.name, it.flashcards) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000L),
                    initialValue = EditCardSetUiState.Loading
                )
                .collect { _uiState.value = it }
        }
    }

}

sealed class EditCardSetUiState {
    data object Loading : EditCardSetUiState()
    data class Idle(val cardSetName: String, val flashCards: List<Flashcard>) : EditCardSetUiState()
    data class Error(val message: String?) : EditCardSetUiState()
}