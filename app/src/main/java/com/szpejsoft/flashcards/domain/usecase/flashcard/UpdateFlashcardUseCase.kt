package com.szpejsoft.flashcards.domain.usecase.flashcard

import com.szpejsoft.flashcards.domain.repository.FlashcardRepository
import javax.inject.Inject

class UpdateFlashcardUseCase
@Inject
constructor(private val flashcardRepository: FlashcardRepository) {
    suspend operator fun invoke(flashcardId: Long, obverse: String, reverse: String) {
        flashcardRepository.update(flashcardId, obverse, reverse)
    }
}