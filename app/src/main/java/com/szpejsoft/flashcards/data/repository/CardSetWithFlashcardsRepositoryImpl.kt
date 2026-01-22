package com.szpejsoft.flashcards.data.repository

import androidx.room.withTransaction
import com.szpejsoft.flashcards.data.db.FlashcardsDb
import com.szpejsoft.flashcards.data.db.entities.DbCardSet
import com.szpejsoft.flashcards.data.mappers.toDb
import com.szpejsoft.flashcards.data.mappers.toDomain
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CardSetWithFlashcardsRepositoryImpl
@Inject
constructor(
    private val db: FlashcardsDb
) : CardSetWithFlashcardsRepository {

    private val cardSetDao = db.cardSetDao()
    private val flashcardDao = db.flashcardDao()

    override fun observe(cardSetId: Long): Flow<CardSetWithFlashcards> =
        cardSetDao.observeCardSetWithFlashcards(cardSetId)
            .map { it.toDomain() }

    override suspend fun update(
        cardSetId: Long,
        cardSetName: String,
        flashcardsToSave: List<Flashcard>,
        flashcardIdsToDelete: List<Long>
    ) {
        db.withTransaction {
            cardSetDao.update(cardSetId, cardSetName)
            if (flashcardIdsToDelete.isNotEmpty()) {
                flashcardDao.delete(flashcardIdsToDelete)
            }
            if (flashcardsToSave.isNotEmpty()) {
                flashcardDao.insert(flashcardsToSave.toDb(cardSetId))
            }
        }
    }

    override suspend fun insert(cardSetName: String, flashcards: List<Flashcard>) {
        db.withTransaction {
            val cardSetId = cardSetDao.insert(DbCardSet(0, cardSetName))
            val flashcardsDb = flashcards.toDb(cardSetId)
            flashcardDao.insert(flashcardsDb)
        }
    }

}
