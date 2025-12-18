package com.szpejsoft.flashcards.common.db

import com.szpejsoft.flashcards.common.db.dao.CardSetDao

interface FlashcardsDb {
    fun cardSetDao(): CardSetDao
}