package com.szpejsoft.flashcards.domain.usecase.cardset

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Suppress("UnusedFlow")
class ObserveCardSetsUseCaseTest : BaseMockKTest() {
    private lateinit var sut: ObserveCardSetsUseCase

    @MockK(relaxed = true)
    private lateinit var cardSetRepository: CardSetRepository

    @Before
    fun setUp() {
        sut = ObserveCardSetsUseCase(cardSetRepository)
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

        val repositoryFlow = flowOf(
            emptyList(),
            initialList,
            updatedList,
        )
        every { cardSetRepository.observeAll() } returns repositoryFlow

        //act & assert
        sut().test {
            Assert.assertEquals(emptyList<CardSet>(), awaitItem())
            Assert.assertEquals(initialList, awaitItem())
            Assert.assertEquals(updatedList, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) { cardSetRepository.observeAll() }
    }

}
