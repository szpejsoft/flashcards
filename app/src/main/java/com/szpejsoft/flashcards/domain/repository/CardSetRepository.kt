package com.szpejsoft.flashcards.domain.repository

import com.szpejsoft.flashcards.domain.model.CardSet
import kotlinx.coroutines.flow.Flow

interface CardSetRepository {
    suspend fun save(cardSetName: String)
    fun observeAll(): Flow<List<CardSet>>
}