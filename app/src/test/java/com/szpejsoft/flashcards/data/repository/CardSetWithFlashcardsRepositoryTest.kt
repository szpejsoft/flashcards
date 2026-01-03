package com.szpejsoft.flashcards.data.repository

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.db.entities.DbCardSet
import com.szpejsoft.flashcards.data.db.entities.DbCardSetWithFlashcards
import com.szpejsoft.flashcards.data.db.entities.DbFlashcard
import com.szpejsoft.flashcards.data.mappers.toDomain
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@Suppress("UnusedFlow")
class CardSetWithFlashcardsRepositoryTest : BaseMockKTest() {
    private lateinit var sut: CardSetWithFlashcardsRepositoryImpl

    @MockK(relaxed = true)
    private lateinit var cardSetDao: CardSetDao

    @Before
    fun setUp() {
        sut = CardSetWithFlashcardsRepositoryImpl(cardSetDao)
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

        //act & assert
        sut.observe(1).test {
            assertEquals(initialSet.toDomain(), awaitItem())
            assertEquals(updatedSet.toDomain(), awaitItem())
            awaitComplete()
        }
        verify(exactly = 1)
        { cardSetDao.observeCardSetWithFlashcards(1) }

    }

}