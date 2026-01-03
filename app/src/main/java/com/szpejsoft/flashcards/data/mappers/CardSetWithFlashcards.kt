package com.szpejsoft.flashcards.data.mappers

import com.szpejsoft.flashcards.data.db.entities.DbCardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards

fun DbCardSetWithFlashcards.toDomain() = CardSetWithFlashcards(
    cardSet = cardSet.toDomain(),
    flashcards = flashcards.toDomain()
)