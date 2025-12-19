package com.szpejsoft.flashcards.data.db

import com.szpejsoft.flashcards.data.db.dao.CardSetDao

interface FlashcardsDb {
    fun cardSetDao(): CardSetDao
}