package com.szpejsoft.flashcards.data.db

import com.szpejsoft.flashcards.data.db.dao.CardSetDao

class RoomFlashcardsDb(private val db: FlashcardsDbDefinition) : FlashcardsDb {
    override fun cardSetDao(): CardSetDao = db.cardSetDao()
}