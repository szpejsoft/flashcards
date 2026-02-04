package com.szpejsoft.flashcards.data.repository

import androidx.room.withTransaction
import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseTest
import com.szpejsoft.flashcards.data.db.FlashcardsDb
import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.db.dao.FlashcardDao
import com.szpejsoft.flashcards.data.db.entities.DbCardSet
import com.szpejsoft.flashcards.data.db.entities.DbCardSetWithFlashcards
import com.szpejsoft.flashcards.data.db.entities.DbFlashcard
import com.szpejsoft.flashcards.data.mappers.toDb
import com.szpejsoft.flashcards.data.mappers.toDomain
import com.szpejsoft.flashcards.domain.model.Flashcard
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@Suppress("UnusedFlow")
class CardSetWithFlashcardsRepositoryTest : BaseTest() {
    private lateinit var sut: CardSetWithFlashcardsRepositoryImpl

    @MockK(relaxed = false)
    private lateinit var db: FlashcardsDb

    @MockK(relaxed = true)
    private lateinit var cardSetDao: CardSetDao

    @MockK(relaxed = true)
    private lateinit var flashcardDao: FlashcardDao


    @Before
    fun setUp() {
        mockkStatic("androidx.room.RoomDatabaseKt")
        val transactionLambda = slot<suspend () -> Any>()
        coEvery { db.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
        every { db.cardSetDao() } returns cardSetDao
        every { db.flashcardDao() } returns flashcardDao

        sut = CardSetWithFlashcardsRepositoryImpl(db)
    }

    @Test
    fun `when observe called repository shows data from DB`() = runTest {
        //arrange
        val initialSet = DbCardSetWithFlashcards(
            cardSet = DbCardSet(1, "cardset1"),
            flashcards = emptyList()
        )
        val updatedSet = DbCardSetWithFlashcards(
            cardSet = DbCardSet(1, "card set 1"),
            flashcards = listOf(DbFlashcard(1, 1, "obverse", "reverse"))
        )
        val daoFlow = flowOf(initialSet, updatedSet)

        every { cardSetDao.observeCardSetWithFlashcards(1) } returns daoFlow
        every { db.cardSetDao() } returns cardSetDao
        sut = CardSetWithFlashcardsRepositoryImpl(db)

        //act & assert
        sut.observe(1).test {
            assertEquals(initialSet.toDomain(), awaitItem())
            assertEquals(updatedSet.toDomain(), awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) { cardSetDao.observeCardSetWithFlashcards(1) }
    }

    @Test
    fun `when update called proper methods on daos called (only set name changed)`() = runTest {
        //arrange
        val cardSetId = 17L
        val cardSetName = "the most random number"
        val flashcardsToSave = emptyList<Flashcard>()
        val flashcardIdsToDelete = emptyList<Long>()

        //act
        sut.update(cardSetId, cardSetName, flashcardsToSave, flashcardIdsToDelete)

        //assert
        coVerify(exactly = 1) { cardSetDao.update(cardSetId, cardSetName) }
        coVerify(exactly = 0) { flashcardDao.delete(any<List<Long>>()) }
        coVerify(exactly = 0) { flashcardDao.insert(any()) }
    }


    @Test
    fun `when update called proper methods on daos called (only delete)`() = runTest {
        //arrange
        val cardSetId = 17L
        val cardSetName = "the most random number"
        val flashcardsToSave = emptyList<Flashcard>()
        val flashcardIdsToDelete = listOf(13L, 17L)

        //act
        sut.update(cardSetId, cardSetName, flashcardsToSave, flashcardIdsToDelete)

        //assert
        coVerify(exactly = 1) { cardSetDao.update(cardSetId, cardSetName) }
        coVerify(exactly = 1) { flashcardDao.delete(listOf(13L, 17L)) }
        coVerify(exactly = 0) { flashcardDao.insert(any()) }
    }

    @Test
    fun `when update called proper methods on daos called (only insert)`() = runTest {
        //arrange
        val cardSetId = 17L
        val cardSetName = "the most random number"
        val flashcardsToSave = listOf(
            Flashcard(13, "obverse", "reverse"),
        )
        val flashcardIdsToDelete = emptyList<Long>()

        //act
        sut.update(cardSetId, cardSetName, flashcardsToSave, flashcardIdsToDelete)

        //assert
        coVerify(exactly = 1) { cardSetDao.update(cardSetId, cardSetName) }
        coVerify(exactly = 0) { flashcardDao.delete(any<List<Long>>()) }
        coVerify(exactly = 1) { flashcardDao.insert(flashcardsToSave.toDb(17L)) }
    }

    @Test
    fun `when insert called proper method on daos are called`() = runTest {
        //arrange
        val cardSetName = "the most random number"
        val flashcardsToSave = listOf(
            Flashcard(0, "obverse", "reverse"),
        )
        coEvery { cardSetDao.insert(any()) } returns 17L

        //act
        sut.insert(cardSetName, flashcardsToSave)

        //assert
        val expectedDbFlashcards = listOf(DbFlashcard(0, 17L, "obverse", "reverse"))

        coVerify(exactly = 1) { cardSetDao.insert(DbCardSet(name = cardSetName)) }
        coVerify(exactly = 1) { flashcardDao.insert(expectedDbFlashcards) }
    }
}


