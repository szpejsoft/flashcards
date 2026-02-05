package com.szpejsoft.flashcards.presentation.learn

import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.model.PracticeMode
import kotlinx.coroutines.flow.StateFlow

interface LearnCardSetViewModel {
    interface Factory {
        fun create(cardSetId: Long): LearnCardSetViewModel
    }

    sealed class UiState {
        data class FlashcardToLearn(
            val setName: String,
            val cardSetSize: Int,
            val learnedCards: Int,
            val flashcardToLearn: Flashcard,
            val practiceMode: PracticeMode,
            val caseSensitive: Boolean
        ) : UiState()

        data object LearningFinished : UiState()
    }

    val uiState: StateFlow<UiState>

    fun onCardLearned()
    fun onCardNotLearned()
    fun onAnswerProvided(answer: String)
    fun onTestModeChanged(newMode: PracticeMode)
    fun onCaseSensitiveChanged(caseSensitive: Boolean)
}
