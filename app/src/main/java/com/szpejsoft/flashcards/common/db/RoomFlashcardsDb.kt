package com.szpejsoft.flashcards.common.db

import com.szpejsoft.flashcards.common.db.dao.CardSetDao

class RoomFlashcardsDb(private val db: FlashcardsDbDefinition) : FlashcardsDb {
    override fun cardSetDao(): CardSetDao = db.cardSetDao()
}