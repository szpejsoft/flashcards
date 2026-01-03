package com.szpejsoft.flashcards.data.repository

import com.szpejsoft.flashcards.data.db.dao.FlashcardDao
import com.szpejsoft.flashcards.data.db.entities.DbFlashcard
import com.szpejsoft.flashcards.domain.repository.FlashcardRepository
import javax.inject.Inject

class FlashcardRepositoryImpl
@Inject
constructor(private val flashcardDao: FlashcardDao) : FlashcardRepository {

    override suspend fun delete(flashcardId: Long) {
        flashcardDao.delete(flashcardId)
    }

    override suspend fun save(cardSetId: Long, obverse: String, reverse: String) {
        flashcardDao.insert(DbFlashcard(id = 0, cardSetId = cardSetId, obverse = obverse, reverse = reverse))
    }

    override suspend fun update(flashcardId: Long, obverse: String, reverse: String) {
        flashcardDao.update(flashcardId, obverse, reverse)
    }

}