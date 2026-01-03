package com.szpejsoft.flashcards.domain.repository

interface FlashcardRepository {
    suspend fun save(cardSetId: Long, obverse: String, reverse: String)
    suspend fun update(flashcardId: Long, obverse: String, reverse: String)
    suspend fun delete(flashcardId: Long)
}