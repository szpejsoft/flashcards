package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import androidx.lifecycle.ViewModel
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.LearnCardSetUiState.FlashcardToLearn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel(assistedFactory = LearnCardSetViewModel.Factory::class)
open class LearnCardSetViewModel
@AssistedInject
constructor(
    @Assisted
    private val cardSetId: Long,
    private val observeCardSetUseCase: ObserveCardSetUseCase,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(cardSetId: Long): LearnCardSetViewModel
    }

    open val uiState: StateFlow<LearnCardSetUiState> get() = _uiState
    private val _uiState = MutableStateFlow<LearnCardSetUiState>(
        FlashcardToLearn(
            setName = "my set",
            cardSetSize = 2,
            learnedCards = 0,
            flashcardToLearn = Flashcard(0, "obverse", "reverse")
        )
    )

    fun onCardLearned() {
        val state = _uiState.value as FlashcardToLearn
        if (state.learnedCards + 1 < state.cardSetSize) {
            _uiState.update { _ ->
                state.copy(learnedCards = state.learnedCards + 1)
            }
        } else {
            _uiState.value = LearnCardSetUiState.Success
        }
    }

}

sealed class LearnCardSetUiState {
    data class FlashcardToLearn(
        val setName: String = "",
        val cardSetSize: Int,
        val learnedCards: Int,
        val flashcardToLearn: Flashcard
    ) : LearnCardSetUiState()

    data object Success : LearnCardSetUiState()
}