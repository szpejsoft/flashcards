package com.szpejsoft.flashcards.domain.repository

import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import kotlinx.coroutines.flow.Flow

interface CardSetWithFlashcardsRepository {
    fun observe(cardSetId: Long): Flow<CardSetWithFlashcards>
}