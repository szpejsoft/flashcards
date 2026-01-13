package com.szpejsoft.flashcards.domain.usecase.cardset

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Suppress("UnusedFlow")
class ObserveCardSetUseCaseTest : BaseMockKTest() {
    private lateinit var sut: ObserveCardSetUseCase

    @MockK(relaxed = true)
    private lateinit var cardSetWithFlashcardsRepository: CardSetWithFlashcardsRepository

    @Before
    fun setUp() {
        sut = ObserveCardSetUseCase(cardSetWithFlashcardsRepository)
    }

    @Test
    fun `when invoke called, usecase shows data from repository`() = runTest {
        //arrange
        val initialSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "cardset1"),
            flashcards = emptyList()
        )
        val updatedSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set 1"),
            flashcards = listOf(Flashcard(1, "obverse", "reverse"))
        )
        val repositoryFlow = flowOf(initialSet, updatedSet)
        every { cardSetWithFlashcardsRepository.observe(1) } returns repositoryFlow

        //act & assert
        sut(1).test {
            Assert.assertEquals(initialSet, awaitItem())
            Assert.assertEquals(updatedSet, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) { cardSetWithFlashcardsRepository.observe(1) }
    }

}
