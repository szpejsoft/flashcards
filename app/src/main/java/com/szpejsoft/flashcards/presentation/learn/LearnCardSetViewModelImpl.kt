package com.szpejsoft.flashcards.presentation.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.FlashcardToLearn
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
            flashcardToLearn = Flashcard()
        )
    )

    private lateinit var cardSetName: String
    private val flashCardsToLearn = mutableListOf<Flashcard>()
    private var setSize: Int = 0

    init {
        viewModelScope.launch {
            val cardSet = observeCardSetUseCase(cardSetId).first()
            cardSetName = cardSet.cardSet.name
            setSize = cardSet.flashcards.size
            flashCardsToLearn.replaceWith(cardSet.flashcards)
            _uiState.update {
                FlashcardToLearn(
                    setName = cardSetName,
                    cardSetSize = setSize,
                    learnedCards = setSize - flashCardsToLearn.size,
                    flashcardToLearn = flashCardsToLearn.getRandom()
                )
            }
        }
    }

    override fun onCardLearned() {
        val state = _uiState.value as FlashcardToLearn
        val learnedCard = state.flashcardToLearn
        flashCardsToLearn.remove(learnedCard)
        if (flashCardsToLearn.isNotEmpty()) {
            _uiState.update {
                state.copy(
                    learnedCards = setSize - flashCardsToLearn.size,
                    flashcardToLearn = flashCardsToLearn.getRandom()
                )
            }
        } else {
            _uiState.value = UiState.LearningFinished
        }
    }

    override fun onCardNotLearned() {
        _uiState.update {
            FlashcardToLearn(
                setName = cardSetName,
                cardSetSize = setSize,
                flashcardToLearn = flashCardsToLearn.getRandom(),
                learnedCards = setSize - flashCardsToLearn.size
            )
        }
    }

}
