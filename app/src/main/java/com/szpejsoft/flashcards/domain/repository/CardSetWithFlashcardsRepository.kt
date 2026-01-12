package com.szpejsoft.flashcards.domain.repository

import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface CardSetWithFlashcardsRepository {
    suspend fun update(
        cardSetId: Long,
        cardSetName: String,
        flashcardsToSave: List<Flashcard>,
        flashcardIdsToDelete: List<Long>
    )

    fun observe(cardSetId: Long): Flow<CardSetWithFlashcards>
}