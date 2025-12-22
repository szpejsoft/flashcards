package com.szpejsoft.flashcards.data.repository

import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.db.entities.DbCardSet
import com.szpejsoft.flashcards.data.mappers.toDomain
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class CardSetRepositoryImpl
@Inject
constructor(
    private val cardSetDao: CardSetDao
) : CardSetRepository {

    override suspend fun delete(cardSetId: Long) {
        cardSetDao.delete(cardSetId)
    }

    override suspend fun save(cardSetName: String) {
        cardSetDao.insert(DbCardSet(id = 0, name = cardSetName))
    }

    override fun observeAll(): Flow<List<CardSet>> =
        cardSetDao.observe()
            .map { it.toDomain() }

}