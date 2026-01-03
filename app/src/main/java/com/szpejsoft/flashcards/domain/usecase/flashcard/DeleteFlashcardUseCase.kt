package com.szpejsoft.flashcards.domain.usecase.flashcard

import com.szpejsoft.flashcards.domain.repository.FlashcardRepository
import javax.inject.Inject

class DeleteFlashcardUseCase
@Inject
constructor(private val flashcardRepository: FlashcardRepository) {
    suspend operator fun invoke(id: Long) = flashcardRepository.delete(id)
}