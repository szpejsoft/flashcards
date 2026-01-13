package com.szpejsoft.flashcards.domain.repository

import com.szpejsoft.flashcards.domain.model.CardSet
import kotlinx.coroutines.flow.Flow

interface CardSetRepository {
    suspend fun delete(cardSetId: Long)
    suspend fun save(cardSetName: String)
    suspend fun update(id: Long, cardSetName: String)
    fun observeAll(): Flow<List<CardSet>>
}
