package com.szpejsoft.flashcards.data.repository

import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.data.db.dao.FlashcardDao
import com.szpejsoft.flashcards.data.db.entities.DbFlashcard
import com.szpejsoft.flashcards.data.repository.FlashcardRepositoryImpl
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FlashcardsRepositoryTest: BaseMockKTest() {
    private lateinit var sut: FlashcardRepositoryImpl

    @MockK(relaxed = true)
    private lateinit var flashcardDao: FlashcardDao

    @Before
    fun setup() {
        sut = FlashcardRepositoryImpl(flashcardDao)
    }

    @Test
    fun `when delete called, dao delete called with proper parameter`() = runTest {
        //arrange

        //act
        sut.delete(42)

        //assert
        coVerify(exactly = 1) { flashcardDao.delete(42) }
    }

    @Test
    fun `when save called, dao save called with proper parameters`() = runTest {
        //arrange
        val cardSetId = 42L
        val obverse = "obverse"
        val reverse = "reverse"
        val dbFlashcardSlot = slot<DbFlashcard>()
        //act
        sut.save(cardSetId, obverse, reverse)

        //assert
        coVerify(exactly = 1) { flashcardDao.insert(capture(dbFlashcardSlot)) }
        assertEquals(cardSetId, dbFlashcardSlot.captured.cardSetId)

    }


}