package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import com.szpejsoft.flashcards.ui.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ObserveCardSetUseCase
@Inject
constructor(private val cardSetWithFlashcardsRepository: CardSetWithFlashcardsRepository) {
    operator fun invoke(id: Long): Flow<CardSetWithFlashcards> =
        cardSetWithFlashcardsRepository.observe(id)
            .onEach { log("usecase $it") }
}