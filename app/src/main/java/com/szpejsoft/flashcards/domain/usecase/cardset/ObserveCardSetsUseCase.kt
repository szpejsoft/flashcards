package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ObserveCardSetsUseCase
@Inject
constructor(private val cardSetRepository: CardSetRepository) {
    operator fun invoke(): Flow<List<CardSet>> = cardSetRepository.observeAll().onEach {
        print("ptsz UC $it")
    }
}