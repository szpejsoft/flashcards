package com.szpejsoft.flashcards.data.db

import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.db.dao.FlashcardDao

class RoomFlashcardsDb(private val db: FlashcardsDbDefinition) : FlashcardsDb {
    override fun cardSetDao(): CardSetDao = db.cardSetDao()
    override fun flashCardDao(): FlashcardDao = db.flashCardDao()
}