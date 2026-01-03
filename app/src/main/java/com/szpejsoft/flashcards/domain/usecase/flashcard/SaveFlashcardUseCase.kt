package com.szpejsoft.flashcards.domain.usecase.flashcard

import com.szpejsoft.flashcards.domain.repository.FlashcardRepository
import javax.inject.Inject

class SaveFlashcardUseCase
@Inject
constructor(private val flashcardRepository: FlashcardRepository) {
    suspend operator fun invoke(cardSetId: Long, obverse: String, reverse: String){
        flashcardRepository.save(cardSetId, obverse, reverse)
    }
}