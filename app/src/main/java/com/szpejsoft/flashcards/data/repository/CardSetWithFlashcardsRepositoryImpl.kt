package com.szpejsoft.flashcards.data.repository

import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.mappers.toDomain
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import com.szpejsoft.flashcards.ui.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CardSetWithFlashcardsRepositoryImpl
@Inject
constructor(
    private val cardSetDao: CardSetDao
) : CardSetWithFlashcardsRepository {

    override fun observe(cardSetId: Long): Flow<CardSetWithFlashcards> =
        cardSetDao.observeCardSetWithFlashcards(cardSetId)
            .map { it.toDomain() }
            .onEach { flashcards -> log("repo $flashcards") }


}