package com.szpejsoft.flashcards.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.db.dao.FlashcardDao
import com.szpejsoft.flashcards.data.db.entities.DbCardSet
import com.szpejsoft.flashcards.data.db.entities.DbFlashcard

@Database(
    version = 1,
    entities = [
        DbCardSet::class,
        DbFlashcard::class
    ]
)
abstract class FlashcardsDb : RoomDatabase(){
    abstract fun cardSetDao(): CardSetDao
    abstract fun flashcardDao(): FlashcardDao
}
