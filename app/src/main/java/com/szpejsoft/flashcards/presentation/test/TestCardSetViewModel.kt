package com.szpejsoft.flashcards.presentation.test

import com.szpejsoft.flashcards.domain.model.Flashcard
import kotlinx.coroutines.flow.StateFlow

interface TestCardSetViewModel {
    interface Factory {
        fun create(cardSetId: Long): TestCardSetViewModel
    }


    sealed class UiState {

        data class FlashcardToTest(
            val setName: String,
            val cardSetSize: Int,
            val learnedCards: Int,
            val failedCards: Int,
            val flashcardToTest: Flashcard,
            val testMode: TestMode,
            val caseSensitive: Boolean
        ) : UiState()

        data class TestFinished(
            val learnedCards: Int,
            val cardSetSize: Int
        ) : UiState()
    }

    enum class TestMode {
        Click, Write
    }

    val uiState: StateFlow<UiState>

    fun onCardLearned()
    fun onCardNotLearned()
    fun onAnswerProvided(answer: String)
    fun onTestModeChanged(newMode: TestMode)
    fun onCaseSensitiveChanged(caseSensitive: Boolean)
}