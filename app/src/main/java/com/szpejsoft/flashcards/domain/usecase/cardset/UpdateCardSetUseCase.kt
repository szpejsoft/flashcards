package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import javax.inject.Inject

class UpdateCardSetUseCase
@Inject
constructor(private val cardSetRepository: CardSetRepository) {
    suspend operator fun invoke(id: Long, cardSetName: String) {
        cardSetRepository.update(id, cardSetName)
    }
}