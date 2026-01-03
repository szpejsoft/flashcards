package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import javax.inject.Inject

class SaveCardSetUseCase
@Inject
constructor(private val cardSetRepository: CardSetRepository) {
    suspend operator fun invoke(cardSetName: String) {
        cardSetRepository.save(cardSetName)
    }
}