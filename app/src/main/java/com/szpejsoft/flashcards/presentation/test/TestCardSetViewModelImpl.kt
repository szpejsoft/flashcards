package com.szpejsoft.flashcards.presentation.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.model.PracticeMode
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState.FlashcardToTest
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState.TestFinished
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

@HiltViewModel(assistedFactory = TestCardSetViewModelImpl.Factory::class)
class TestCardSetViewModelImpl
@AssistedInject
constructor(
    @Assisted
    private val cardSetId: Long,
    private val observeCardSetUseCase: ObserveCardSetUseCase,
) : ViewModel(), TestCardSetViewModel {

    @AssistedFactory
    interface Factory : TestCardSetViewModel.Factory {
        override fun create(cardSetId: Long): TestCardSetViewModelImpl
    }

    override val uiState: StateFlow<UiState> get() = _uiState
    private val _uiState = MutableStateFlow<UiState>(
        FlashcardToTest(
            setName = "",
            cardSetSize = 0,
            learnedCards = 0,
            failedCards = 0,
            flashcardToTest = Flashcard(),
            practiceMode = PracticeMode.Click,
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
                    practiceMode = PracticeMode.Click,
                    caseSensitive = true
                )
            }
        }
    }

    override fun onCardLearned() {
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

    override fun onCardNotLearned() {
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

    override fun onAnswerProvided(answer: String) {
        val state = _uiState.value as FlashcardToTest
        val expectedAnswer = state.flashcardToTest.reverse
        if (checkAnswer(answer, expectedAnswer, state.caseSensitive)) {
            onCardLearned()
        } else {
            onCardNotLearned()
        }
    }

    override fun onTestModeChanged(newMode: PracticeMode) {
        _uiState.update { (it as FlashcardToTest).copy(practiceMode = newMode) }
    }

    override fun onCaseSensitiveChanged(caseSensitive: Boolean) {
        _uiState.update { (it as FlashcardToTest).copy(caseSensitive = caseSensitive) }
    }

    private fun checkAnswer(answer: String, expectedAnswer: String, caseSensitive: Boolean) =
        expectedAnswer.equals(answer, !caseSensitive)

}
