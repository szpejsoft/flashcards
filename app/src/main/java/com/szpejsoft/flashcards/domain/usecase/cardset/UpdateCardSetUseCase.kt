package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import javax.inject.Inject

class UpdateCardSetUseCase
@Inject
constructor(private val cardSetRepository: CardSetWithFlashcardsRepository) {

    suspend operator fun invoke(
        cardSetId: Long,
        cardSetName: String,
        flashcardsToSave: List<Flashcard>,
        flashcardIdsToDelete: List<Long>
    ) {
        if (cardSetName.isBlank()) throw IllegalArgumentException("Card set name cannot be blank")
        cardSetRepository.update(
            cardSetId,
            cardSetName,
            flashcardsToSave,
            flashcardIdsToDelete
        )
    }
}
