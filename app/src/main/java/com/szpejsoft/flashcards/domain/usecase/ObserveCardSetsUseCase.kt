package com.szpejsoft.flashcards.domain.usecase

import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import javax.inject.Inject

class ObserveCardSetsUseCase
@Inject
constructor(private val cardSetRepository: CardSetRepository) {
    operator fun invoke() = cardSetRepository.observeAll()
}