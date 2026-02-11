package com.szpejsoft.flashcards.presentation.learn

import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.model.PracticeMode
import kotlinx.coroutines.flow.StateFlow

interface LearnCardSetViewModel {
    interface Factory {
        fun create(cardSetId: Long): LearnCardSetViewModel
    }

    sealed interface UiState {
        sealed interface LearningInProgress : UiState {
            val setName: String
            val cardSetSize: Int
            val learnedCards: Int
            val flashcardToLearn: Flashcard
            val learnMode: PracticeMode
            val caseSensitive: Boolean
        }

        data class FlashcardToLearn(
            override val setName: String,
            override val cardSetSize: Int,
            override val learnedCards: Int,
            override val flashcardToLearn: Flashcard,
            override val learnMode: PracticeMode,
            override val caseSensitive: Boolean,
            val showSuccessToast: Boolean
        ) : LearningInProgress

        data class WrongAnswer(
            override val setName: String,
            override val cardSetSize: Int,
            override val learnedCards: Int,
            override val flashcardToLearn: Flashcard,
            override val learnMode: PracticeMode,
            override val caseSensitive: Boolean,
            val providedAnswer: String,
        ) : LearningInProgress

        data object LearningFinished : UiState

    }

    val uiState: StateFlow<UiState>

    fun onCardLearned()
    fun onCardNotLearned()
    fun onAnswerProvided(answer: String)
    fun onTestModeChanged(newMode: PracticeMode)
    fun onCaseSensitiveChanged(caseSensitive: Boolean)
    fun onToastShown()
}
