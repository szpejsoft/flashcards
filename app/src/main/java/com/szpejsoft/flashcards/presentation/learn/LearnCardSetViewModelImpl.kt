package com.szpejsoft.flashcards.presentation.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.model.PracticeMode
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.FlashcardToLearn
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.LearningFinished
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.WrongAnswer
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

@HiltViewModel(assistedFactory = LearnCardSetViewModelImpl.Factory::class)
class LearnCardSetViewModelImpl
@AssistedInject
constructor(
    @Assisted
    private val cardSetId: Long,
    private val observeCardSetUseCase: ObserveCardSetUseCase,
) : ViewModel(), LearnCardSetViewModel {

    @AssistedFactory
    interface Factory : LearnCardSetViewModel.Factory {
        override fun create(cardSetId: Long): LearnCardSetViewModelImpl
    }

    override val uiState: StateFlow<UiState> get() = _uiState
    private val _uiState = MutableStateFlow<UiState>(
        FlashcardToLearn(
            setName = "",
            cardSetSize = 0,
            learnedCards = 0,
            flashcardToLearn = Flashcard(),
            learnMode = PracticeMode.Click,
            caseSensitive = true,
            showSuccessToast = false
        )
    )

    private lateinit var cardSetName: String
    private val flashcardsToLearn = mutableListOf<Flashcard>()
    private var setSize: Int = 0

    init {
        viewModelScope.launch {
            val cardSet = observeCardSetUseCase(cardSetId).first()
            cardSetName = cardSet.cardSet.name
            setSize = cardSet.flashcards.size
            flashcardsToLearn.replaceWith(cardSet.flashcards)
            _uiState.update {
                FlashcardToLearn(
                    setName = cardSetName,
                    cardSetSize = setSize,
                    learnedCards = setSize - flashcardsToLearn.size,
                    flashcardToLearn = flashcardsToLearn.getRandom(),
                    learnMode = PracticeMode.Click,
                    caseSensitive = true,
                    showSuccessToast = false
                )
            }
        }
    }

    override fun onCardLearned() {
        val state = _uiState.value as UiState.LearningInProgress
        val learnedCard = state.flashcardToLearn
        flashcardsToLearn.remove(learnedCard)
        if (flashcardsToLearn.isNotEmpty()) {
            _uiState.update {
                state.copy(
                    learnedCards = setSize - flashcardsToLearn.size,
                    flashcardToLearn = flashcardsToLearn.getRandom(),
                    showSuccessToast = true
                )
            }
        } else {
            _uiState.value = LearningFinished
        }
    }

    override fun onCardNotLearned() {
        when (val state = _uiState.value as UiState.LearningInProgress) {
            is FlashcardToLearn -> _uiState.update {
                WrongAnswer(
                    setName = state.setName,
                    cardSetSize = state.cardSetSize,
                    learnedCards = state.learnedCards,
                    flashcardToLearn = state.flashcardToLearn,
                    learnMode = state.learnMode,
                    caseSensitive = state.caseSensitive,
                    providedAnswer = "-"
                )
            }

            is WrongAnswer -> _uiState.update {
                FlashcardToLearn(
                    setName = state.setName,
                    cardSetSize = state.cardSetSize,
                    learnedCards = state.learnedCards,
                    flashcardToLearn = flashcardsToLearn.getRandom(),
                    learnMode = state.learnMode,
                    caseSensitive = state.caseSensitive,
                    showSuccessToast = false
                )
            }
        }
    }

    override fun onToastShown() {
        val state = _uiState.value as FlashcardToLearn
        _uiState.update {
            state.copy(showSuccessToast = false)
        }
    }

    override fun onAnswerProvided(answer: String) {
        val state = _uiState.value as FlashcardToLearn
        val expectedAnswer = state.flashcardToLearn.reverse
        if (checkAnswer(answer, expectedAnswer, state.caseSensitive)) {
            onCardLearned()
        } else {
            showWrongAnswer(answer)
        }
    }

    override fun onTestModeChanged(newMode: PracticeMode) {
        _uiState.update { (it as FlashcardToLearn).copy(learnMode = newMode) }
    }

    override fun onCaseSensitiveChanged(caseSensitive: Boolean) {
        _uiState.update { (it as FlashcardToLearn).copy(caseSensitive = caseSensitive) }
    }

    private fun showWrongAnswer(answer: String) {
        val state = _uiState.value as FlashcardToLearn
        _uiState.update {
            state.run {
                WrongAnswer(
                    setName = setName,
                    providedAnswer = answer,
                    flashcardToLearn = flashcardToLearn,
                    cardSetSize = setSize,
                    learnedCards = learnedCards,
                    learnMode = learnMode,
                    caseSensitive = caseSensitive,
                )
            }
        }
    }

    private fun checkAnswer(answer: String, expectedAnswer: String, caseSensitive: Boolean) =
        expectedAnswer.equals(answer, !caseSensitive)

    private fun UiState.LearningInProgress.copy(
        showSuccessToast: Boolean? = null,
        flashcardToLearn: Flashcard? = null,
        learnedCards: Int? = null,
    ): UiState.LearningInProgress = when (this) {
        is FlashcardToLearn -> copy(
            showSuccessToast = showSuccessToast ?: this.showSuccessToast,
            flashcardToLearn = flashcardToLearn ?: this.flashcardToLearn,
            learnedCards = learnedCards ?: this.learnedCards,
        )

        is WrongAnswer -> copy(
            flashcardToLearn = flashcardToLearn ?: this.flashcardToLearn,
            learnedCards = learnedCards ?: this.learnedCards,
        )
    }

}
