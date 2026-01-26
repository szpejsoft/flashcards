package com.szpejsoft.flashcards.ui.screens.cardsets.test.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.test.test.TestCardSetUiState.FlashcardToTest
import com.szpejsoft.flashcards.ui.screens.cardsets.test.test.TestCardSetUiState.TestFinished
import com.szpejsoft.flashcards.ui.screens.getRandom
import com.szpejsoft.flashcards.ui.screens.replaceWith
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TestCardSetViewModel.Factory::class)
open class TestCardSetViewModel
@AssistedInject
constructor(
    @Assisted
    private val cardSetId: Long,
    private val observeCardSetUseCase: ObserveCardSetUseCase,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(cardSetId: Long): TestCardSetViewModel
    }

    open val uiState: StateFlow<TestCardSetUiState> get() = _uiState
    private val _uiState = MutableStateFlow<TestCardSetUiState>(
        FlashcardToTest(
            setName = "",
            cardSetSize = 0,
            learnedCards = 0,
            failedCards = 0,
            flashcardToTest = Flashcard(),
            testMode = TestMode.Click,
            caseSensitive = true
        )
    )

    private lateinit var cardSetName: String
    private val flashCardsToLearn = mutableListOf<Flashcard>()
    private var setSize: Int = 0
    private var learned: Int = 0
    private var failed: Int = 0

    init {
        viewModelScope.launch {
            val cardSet = observeCardSetUseCase(cardSetId).first()
            cardSetName = cardSet.cardSet.name
            setSize = cardSet.flashcards.size
            flashCardsToLearn.replaceWith(cardSet.flashcards)
            _uiState.update {
                FlashcardToTest(
                    setName = cardSetName,
                    cardSetSize = setSize,
                    learnedCards = 0,
                    failedCards = 0,
                    flashcardToTest = flashCardsToLearn.getRandom(),
                    testMode = TestMode.Click,
                    caseSensitive = true
                )
            }
        }
    }

    open fun onCardLearned() {
        val state = _uiState.value as FlashcardToTest
        val testedCard = state.flashcardToTest
        learned++
        flashCardsToLearn.remove(testedCard)
        if (flashCardsToLearn.isNotEmpty()) {
            _uiState.update {
                state.copy(
                    learnedCards = learned,
                    flashcardToTest = flashCardsToLearn.getRandom()
                )
            }
        } else {
            _uiState.value = TestFinished(learned, setSize)
        }
    }

    open fun onCardNotLearned() {
        val state = _uiState.value as FlashcardToTest
        val testedCard = state.flashcardToTest
        failed++
        flashCardsToLearn.remove(testedCard)
        if (flashCardsToLearn.isNotEmpty()) {
            _uiState.update {
                state.copy(
                    failedCards = failed,
                    flashcardToTest = flashCardsToLearn.getRandom()
                )
            }
        } else {
            _uiState.value = TestFinished(learned, setSize)
        }
    }

    open fun onAnswerProvided(answer: String) {
        val state = _uiState.value as FlashcardToTest
        val expectedAnswer = state.flashcardToTest.reverse
        if (checkAnswer(answer, expectedAnswer, state.caseSensitive)) {
            onCardLearned()
        } else {
            onCardNotLearned()
        }
    }

    private fun checkAnswer(answer: String, expectedAnswer: String, caseSensitive: Boolean) =
        expectedAnswer.equals(answer, !caseSensitive)

    open fun onTestModeChanged(newMode: TestMode) {
        _uiState.update { (it as FlashcardToTest).copy(testMode = newMode) }
    }

    open fun onCaseSensitiveChanged(caseSensitive: Boolean) {
        _uiState.update { (it as FlashcardToTest).copy(caseSensitive = caseSensitive) }
    }

}

sealed class TestCardSetUiState {

    data class FlashcardToTest(
        val setName: String,
        val cardSetSize: Int,
        val learnedCards: Int,
        val failedCards: Int,
        val flashcardToTest: Flashcard,
        val testMode: TestMode,
        val caseSensitive: Boolean
    ) : TestCardSetUiState()

    data class TestFinished(
        val learnedCards: Int,
        val cardSetSize: Int
    ) : TestCardSetUiState()
}

enum class TestMode {
    Click, Write
}