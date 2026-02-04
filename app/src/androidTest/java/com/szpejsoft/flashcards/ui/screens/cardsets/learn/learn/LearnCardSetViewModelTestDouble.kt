package com.szpejsoft.flashcards.ui.screens.cardsets.learn.learn

import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.FlashcardToLearn
import kotlinx.coroutines.flow.MutableStateFlow

class LearnCardSetViewModelTestDouble : LearnCardSetViewModel {

    var onCardLearnedCalled = false
        private set
    var onCardNotLearnedCalled = false
        private set

    override val uiState: MutableStateFlow<UiState> = MutableStateFlow(
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
