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
import timber.log.Timber
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
        Timber.d("ptsz repo update: ${flashcardsToSave.joinToString(prefix = "\n ", separator = ", ")} ")
        db.withTransaction {
            Timber.d("ptsz repo update in transaction 1 ")
            cardSetDao.update(cardSetId, cardSetName)
            if (flashcardIdsToDelete.isNotEmpty()) {
                flashcardDao.delete(flashcardIdsToDelete)
            }
            Timber.d("ptsz repo update in transaction 2 ")
            if (flashcardsToSave.isNotEmpty()) {
                flashcardDao.upsert(flashcardsToSave.toDb(cardSetId))
            }
            Timber.d("ptsz repo update in transaction 3 ")
        }
    }

    override suspend fun insert(cardSetName: String, flashcards: List<Flashcard>) {
        db.withTransaction {
            val cardSetId = cardSetDao.insert(DbCardSet(name = cardSetName))
            val flashcardsDb = flashcards.toDb(cardSetId)
            flashcardDao.upsert(flashcardsDb)
        }
    }

}
