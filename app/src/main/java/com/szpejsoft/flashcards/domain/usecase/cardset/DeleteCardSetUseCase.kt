package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import javax.inject.Inject

class DeleteCardSetUseCase
@Inject
constructor(private val cardSetRepository: CardSetRepository) {
    suspend operator fun invoke(cardSetId: Long) = cardSetRepository.delete(cardSetId)
}