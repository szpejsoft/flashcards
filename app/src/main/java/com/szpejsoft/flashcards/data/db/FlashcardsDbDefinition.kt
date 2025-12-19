package com.szpejsoft.flashcards.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.db.entities.DbCardSet

@Database(
    version = 1,
    entities = [
        DbCardSet::class,
    ]
)

abstract class FlashcardsDbDefinition : RoomDatabase() {
    abstract fun cardSetDao(): CardSetDao
}