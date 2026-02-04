package com.szpejsoft.flashcards.ui.screens.cardsets.test.test

import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.ui.screens.cardsets.test.test.TestCardSetUiState.FlashcardToTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class TestCardSetViewModelTestDouble : TestCardSetViewModel(
    cardSetId = 1L,
    observeCardSetUseCase = mockk(relaxed = true) {
        every { this@mockk(any()) } returns flowOf(dummyCardSet)
    }
) {

    var onCardLearnedCalled = false
        private set

    var onCardNotLearnedCalled = false
        private set

    override val uiState: MutableStateFlow<TestCardSetUiState> = MutableStateFlow(
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

    override fun onCardLearned() {
        onCardLearnedCalled = true

    }

    override fun onCardNotLearned() {
        onCardNotLearnedCalled = true
    }

    override fun onAnswerProvided(answer: String) {
        TODO()
    }

    override fun onTestModeChanged(newMode: TestMode) {
        TODO()
    }

    override fun onCaseSensitiveChanged(caseSensitive: Boolean) {
        TODO()
    }

    companion object {
        private val dummyCardSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "name"),
            flashcards = listOf(Flashcard(1, "q", "a"))
        )

    }

}

