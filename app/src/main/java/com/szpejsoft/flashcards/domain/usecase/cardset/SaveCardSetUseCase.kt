package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import javax.inject.Inject

class SaveCardSetUseCase
@Inject
constructor(private val cardSetWithFlashcardsRepository: CardSetWithFlashcardsRepository) {
    suspend operator fun invoke(cardSetName: String, flashcards: List<Flashcard>) {
        cardSetWithFlashcardsRepository.insert(cardSetName, flashcards.map { it.copy(id = 0) })
    }
}
