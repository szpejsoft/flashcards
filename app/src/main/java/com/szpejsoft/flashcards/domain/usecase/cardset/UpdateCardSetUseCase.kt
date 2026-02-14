package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import timber.log.Timber
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
        require(cardSetName.isNotBlank()) { "Card set name cannot be blank" }
        Timber.d("ptsz uc update: ${flashcardsToSave.joinToString(prefix = "\n ", separator = ", ")} ")
        cardSetRepository.update(
            cardSetId,
            cardSetName,
            flashcardsToSave.map { it.copy(id = it.sanitizeId()) },
            flashcardIdsToDelete
        )
    }

    private fun Flashcard.sanitizeId() = if (id > 0) id else 0

}
