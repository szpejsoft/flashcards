package com.szpejsoft.flashcards.ui.screens.cardsets.test

import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.model.PracticeMode
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState.FlashcardToTest
import kotlinx.coroutines.flow.MutableStateFlow


class TestCardSetViewModelTestDouble : TestCardSetViewModel {
    val onAnswerProvidedCalls: List<String> get() = _onAnswerProvidedCalls
    val onPracticeModeChangedCalls: List<PracticeMode> get() = _onPracticeModeChangedCalls
    val onCaseSensitiveChangedCalls: List<Boolean> get() = _onCaseSensitiveChangedCalls

    var onCardLearnedCalled = false
        private set

    var onCardNotLearnedCalled = false
        private set

    private val _onAnswerProvidedCalls = mutableListOf<String>()
    private val _onPracticeModeChangedCalls = mutableListOf<PracticeMode>()
    private val _onCaseSensitiveChangedCalls = mutableListOf<Boolean>()

    override val uiState: MutableStateFlow<UiState> = MutableStateFlow(
        FlashcardToTest(
            setName = "",
            cardSetSize = 0,
            learnedCards = 0,
            failedCards = 0,
            flashcardToTest = Flashcard(),
            testMode = PracticeMode.Click,
            caseSensitive = true
        )
    )

    override fun onCardLearned() {
        onCardLearnedCalled = true
    }

    override fun onCardNotLearned() {
        onCardNotLearnedCalled = true
    }

    override fun onAnswerProvided(answer: String) {
        _onAnswerProvidedCalls.add(answer)
    }

    override fun onTestModeChanged(newMode: PracticeMode) {
        _onPracticeModeChangedCalls.add(newMode)
    }

    override fun onCaseSensitiveChanged(caseSensitive: Boolean) {
        _onCaseSensitiveChangedCalls.add(caseSensitive)
    }

}
