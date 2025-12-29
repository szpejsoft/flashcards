package com.szpejsoft.flashcards.data.db

import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.db.dao.FlashcardDao

interface FlashcardsDb {
    fun cardSetDao(): CardSetDao
    fun flashCardDao(): FlashcardDao
}