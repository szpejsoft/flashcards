package com.szpejsoft.flashcards.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class DbCardSetWithFlashcards(
    @Embedded
    val cardSet: DbCardSet,
    @Relation(parentColumn = "id", entityColumn = "cardSetId")
    val flashcards: List<DbFlashcard>
)
