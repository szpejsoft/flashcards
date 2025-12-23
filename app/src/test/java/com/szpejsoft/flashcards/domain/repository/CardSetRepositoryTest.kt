package com.szpejsoft.flashcards.domain.repository

import app.cash.turbine.test
import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.db.entities.DbCardSet
import com.szpejsoft.flashcards.data.mappers.toDomain
import com.szpejsoft.flashcards.data.repository.CardSetRepositoryImpl
import com.szpejsoft.flashcards.domain.model.CardSet
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CardSetRepositoryTest {
    private lateinit var sut: CardSetRepositoryImpl
    private lateinit var cardSetDao: CardSetDao

    @Before
    fun setUp() {
        cardSetDao = mockk(relaxed = true)
        sut = CardSetRepositoryImpl(cardSetDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when delete called, dao delete called with proper parameter`() = runTest {
        //arrange

        //act
        sut.delete(1)
        //assert
        coVerify { cardSetDao.delete(1) }
    }

    @Test
    fun `when save called, dao insert called with proper parameter`() = runTest {
        //arrange
        val cardSetName = "test"

        //act
        sut.save(cardSetName)

        //assert
        coVerify { cardSetDao.insert(DbCardSet(0, cardSetName)) }
    }

    @Test
    fun `when observe called, repository shows data from DB`() = runTest {
        //arrange
        val initialList = listOf(
            DbCardSet(1, "name1"),
            DbCardSet(2, "name2")
        )

        val updatedList = listOf(
            DbCardSet(1, "name 1"),
            DbCardSet(2, "name 2")
        )

        val daoFlow = flow {
            emit(emptyList())
            emit(initialList)
            emit(updatedList)
        }
        every { cardSetDao.observeAll() } returns daoFlow

        //act & assert
        sut.observeAll().test {
            assertEquals(emptyList<CardSet>(), awaitItem())
            assertEquals(initialList.toDomain(), awaitItem())
            assertEquals(updatedList.toDomain(), awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) { cardSetDao.observeAll() }
    }

}