package com.szpejsoft.flashcards.screens.cardsets.test.test

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.test.test.TestCardSetUiState
import com.szpejsoft.flashcards.ui.screens.cardsets.test.test.TestCardSetViewModel
import com.szpejsoft.flashcards.ui.screens.getRandom
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestCardSetViewModelTest : BaseMockKTest() {

    @MockK(relaxed = true)
    private lateinit var observeCardSetUseCase: ObserveCardSetUseCase

    private lateinit var sut: TestCardSetViewModel

    @Suppress("UnusedFlow")
    @Test
    fun `when viewmodel is created it reads proper data`() = runTest {
        //arrange
        val cardSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(Flashcard(1, "obverse 1", "reverse 1"))
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet)

        //act
        sut = TestCardSetViewModel(1, observeCardSetUseCase)
        advanceUntilIdle()

        //assert
        coVerify(exactly = 1) { observeCardSetUseCase(1) }
    }

    @Test
    fun `when usecase emits data, viewmodel shows first emission`() = runTest {
        //arrange
        val cardSet1 = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(Flashcard(1, "obverse 1", "reverse 1"))
        )

        val cardSet2 = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(Flashcard(2, "obverse 2", "reverse 2"))
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet1, cardSet2)

        sut = TestCardSetViewModel(1, observeCardSetUseCase)

        //act & assert
        sut.uiState.test {
            awaitItem() //skip first -empty- emission
            val state = awaitItem() as TestCardSetUiState.FlashcardToTest
            assertEquals("card set name", state.setName)
            assertEquals("obverse 1", state.flashcardToTest.obverse)
            assertEquals("reverse 1", state.flashcardToTest.reverse)
            assertEquals(1, state.cardSetSize)
            assertEquals(0, state.learnedCards)
            assertEquals(0, state.failedCards)
        }

    }

    @Test
    fun `when on card learned is called next flashcard is emitted`() = runTest {
        //arrange
        mockkStatic("com.szpejsoft.flashcards.ui.screens.UtilsKt")

        val card1 = Flashcard(1, "obverse 1", "reverse 1")
        val card2 = Flashcard(2, "obverse 2", "reverse 2")
        val cardList = listOf(card1, card2)
        every { cardList.getRandom() } returns card2 andThen card1

        val cardSet1 = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = cardList
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet1)
        sut = TestCardSetViewModel(1, observeCardSetUseCase)
        advanceUntilIdle()

        //act & assert
        sut.uiState.test {
            awaitItem()//skip first -empty- emission
            val state1 = awaitItem() as TestCardSetUiState.FlashcardToTest
            assertEquals(2, state1.flashcardToTest.id)

            sut.onCardLearned()
            advanceUntilIdle()
            val state2 = awaitItem() as TestCardSetUiState.FlashcardToTest
            assertEquals(1, state2.flashcardToTest.id)
            assertEquals(2, state2.cardSetSize)
            assertEquals(1, state2.learnedCards)
            assertEquals(0, state2.failedCards)
        }
        unmockkStatic("com.szpejsoft.flashcards.ui.screens.UtilsKt")
    }

    @Test
    fun `when on card not learned is called next flashcard is emitted`() = runTest {
        //arrange
        mockkStatic("com.szpejsoft.flashcards.ui.screens.UtilsKt")

        val card1 = Flashcard(1, "obverse 1", "reverse 1")
        val card2 = Flashcard(2, "obverse 2", "reverse 2")
        val card3 = Flashcard(3, "obverse 3", "reverse 3")
        val cardList = listOf(card1, card2, card3)
        every { cardList.getRandom() } returns card2 andThen card1 andThen card3


        val cardSet1 = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = cardList
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet1)
        sut = TestCardSetViewModel(1, observeCardSetUseCase)
        advanceUntilIdle()

        //act & assert
        sut.uiState.test {
            println("ptsz ${awaitItem()}") //skip first -empty- emission
            val state1 = awaitItem() as TestCardSetUiState.FlashcardToTest
            println("ptsz ${state1}")

            assertEquals(2, state1.flashcardToTest.id)

            sut.onCardNotLearned()
            advanceUntilIdle()
            val state2 = awaitItem() as TestCardSetUiState.FlashcardToTest
            println("ptsz ${state2}")
            assertEquals(1, state2.flashcardToTest.id)
            assertEquals(3, state2.cardSetSize)
            assertEquals(1, state2.failedCards)
            assertEquals(0, state2.learnedCards)
        }
        unmockkStatic("com.szpejsoft.flashcards.ui.screens.UtilsKt")
    }

    @Test
    fun `when learning finished emit proper state`() = runTest {
        //arrange
        val card1 = Flashcard(1, "obverse 1", "reverse 1")
        val cardSet1 = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(card1)
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet1)
        sut = TestCardSetViewModel(1, observeCardSetUseCase)
        advanceUntilIdle()

        //act & assert
        sut.uiState.test {
            awaitItem() //emit first flashcard to learn
            advanceUntilIdle()

            sut.onCardLearned()
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue(state is TestCardSetUiState.TestFinished)
        }
    }

}
