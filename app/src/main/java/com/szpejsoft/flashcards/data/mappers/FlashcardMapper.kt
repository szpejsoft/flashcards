package com.szpejsoft.flashcards.data.mappers

import com.szpejsoft.flashcards.data.db.entities.DbFlashcard
import com.szpejsoft.flashcards.domain.model.Flashcard

fun DbFlashcard.toDomain() = Flashcard(
    id = id,
    obverse = obverse,
    reverse = reverse
)

fun List<DbFlashcard>.toDomain() = map { it.toDomain() }

fun Flashcard.toDb(cardSetId: Long) = DbFlashcard(
    id = id,
    cardSetId = cardSetId,
    obverse = obverse,
    reverse = reverse
)

fun List<Flashcard>.toDb(cardSetId: Long) = map { it.toDb(cardSetId) }
