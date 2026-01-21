package com.szpejsoft.flashcards.ui.screens.cardsets.learn.learn

import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.learn.LearnCardSetUiState.FlashcardToLearn
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class LearnCardSetViewModelTestDouble : LearnCardSetViewModel(
    cardSetId = 1L,
    observeCardSetUseCase = mockk(relaxed = true) {
        every { this@mockk(any()) } returns flowOf(dummyCardSet)
    }
) {

    var onCardLearnedCalled = false
        private set
    var onCardNotLearnedCalled = false
        private set

    override val uiState: MutableStateFlow<LearnCardSetUiState> = MutableStateFlow(
        FlashcardToLearn(
            setName = "",
            cardSetSize = 0,
            learnedCards = 0,
            flashcardToLearn = Flashcard()
        )
    )

    override fun onCardLearned() {
        onCardLearnedCalled = true

    }

    override fun onCardNotLearned() {
        onCardNotLearnedCalled = true
    }
}

val dummyCardSet = CardSetWithFlashcards(
    cardSet = CardSet(1, "name"),
    flashcards = listOf(Flashcard(1, "q", "a"))
)
