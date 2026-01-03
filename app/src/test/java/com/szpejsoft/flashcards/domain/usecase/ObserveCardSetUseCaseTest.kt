package com.szpejsoft.flashcards.domain.usecase

import app.cash.turbine.test
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@Suppress("UnusedFlow")
class ObserveCardSetUseCaseTest {
    private lateinit var sut: ObserveCardSetsUseCase

    @MockK(relaxed = true)
    private lateinit var cardSetRepository: CardSetRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = ObserveCardSetsUseCase(cardSetRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when invoke called, usecase shows data from repository`() = runTest {
        //arrange
        val initialList = listOf(
            CardSet(1, "name1"),
            CardSet(2, "name2")
        )

        val updatedList = listOf(
            CardSet(1, "name 1"),
            CardSet(2, "name 2")
        )

        val repositoryFlow = flow {
            emit(emptyList())
            emit(initialList)
            emit(updatedList)
        }
        every { cardSetRepository.observeAll() } returns repositoryFlow

        //act & assert
        sut().test {
            assertEquals(emptyList<CardSet>(), awaitItem())
            assertEquals(initialList, awaitItem())
            assertEquals(updatedList, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) { cardSetRepository.observeAll() }
    }

}