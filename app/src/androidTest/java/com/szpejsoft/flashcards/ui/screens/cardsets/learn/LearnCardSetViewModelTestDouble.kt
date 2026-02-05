package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.model.PracticeMode
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.FlashcardToLearn
import kotlinx.coroutines.flow.MutableStateFlow

class LearnCardSetViewModelTestDouble : LearnCardSetViewModel {
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
        FlashcardToLearn(
            setName = "",
            cardSetSize = 0,
            learnedCards = 0,
            flashcardToLearn = Flashcard(),
            learnMode = PracticeMode.Click,
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
