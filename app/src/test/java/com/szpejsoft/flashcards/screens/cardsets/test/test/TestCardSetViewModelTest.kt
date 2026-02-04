package com.szpejsoft.flashcards.screens.cardsets.test.test

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseTest
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.presentation.test.TestCardSetUiState
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel
import com.szpejsoft.flashcards.presentation.test.TestMode
import com.szpejsoft.flashcards.presentation.test.TestMode.Click
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestCardSetViewModelTest : BaseTest() {

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
        advanceUntilIdle()

        //act & assert
        sut.uiState.test {
            val state = awaitItem() as TestCardSetUiState.FlashcardToTest
            assertEquals("card set name", state.setName)
            assertEquals("obverse 1", state.flashcardToTest.obverse)
            assertEquals("reverse 1", state.flashcardToTest.reverse)
            assertEquals(1, state.cardSetSize)
            assertEquals(0, state.learnedCards)
            assertEquals(0, state.failedCards)
            assertEquals(Click, state.testMode)
            assertTrue(state.caseSensitive)
        }

    }

    @Test
    fun `when on card learned is called next flashcard is emitted`() = runTest {
        //arrange
        mockkStatic("com.szpejsoft.flashcards.ui.screens.UtilsKt")

        val card1 = Flashcard(1, "obverse 1", "reverse 1")
        val card2 = Flashcard(2, "obverse 2", "reverse 2")
        val cardList = listOf(card1, card2)
        every { any<List<Flashcard>>().getRandom() } returns card2 andThen card1

        val cardSet1 = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = cardList
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet1)
        sut = TestCardSetViewModel(1, observeCardSetUseCase)


        //act & assert
        sut.uiState.test {
            skipItems(1)
            val state1 = awaitItem() as TestCardSetUiState.FlashcardToTest
            assertEquals(2, state1.flashcardToTest.id)
            sut.onCardLearned()
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
        every { any<List<Flashcard>>().getRandom() } returns card2 andThen card1 andThen card3

        val cardSet1 = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = cardList
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet1)
        sut = TestCardSetViewModel(1, observeCardSetUseCase)

        //act & assert
        sut.uiState.test {
            skipItems(2)
            sut.onCardNotLearned()
            val state = awaitItem() as TestCardSetUiState.FlashcardToTest
            println("3 $state")
            assertEquals("card id", 1, state.flashcardToTest.id)
            assertEquals("cardset size", 3, state.cardSetSize)
            assertEquals("failed", 1, state.failedCards)
            assertEquals("learned ", 0, state.learnedCards)
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

    @Test
    fun `when test mode changed emit proper state`() = runTest {
        //arrange
        val cardSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(Flashcard(1, "obverse 1", "reverse 1"))
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet)
        sut = TestCardSetViewModel(1, observeCardSetUseCase)
        advanceUntilIdle()

        //act & assert
        sut.uiState.test {
            awaitItem() //emit first flashcard to learn
            sut.onTestModeChanged(TestMode.Write)
            val state = awaitItem()
            assertTrue(state is TestCardSetUiState.FlashcardToTest)
            assertEquals(TestMode.Write, (state as TestCardSetUiState.FlashcardToTest).testMode)
        }
    }

    @Test
    fun `when case sensitive changed emit proper state`() = runTest {
        //arrange
        val cardSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(Flashcard(1, "obverse 1", "reverse 1"))
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet)
        sut = TestCardSetViewModel(1, observeCardSetUseCase)
        advanceUntilIdle()

        //act & assert
        sut.uiState.test {
            awaitItem() //emit first flashcard to learn
            sut.onCaseSensitiveChanged(false)
            val state = awaitItem()
            assertTrue(state is TestCardSetUiState.FlashcardToTest)
            assertFalse((state as TestCardSetUiState.FlashcardToTest).caseSensitive)
        }
    }

    @Test
    fun `when in case sensitive mode dont accept answer in wrong case`() = runTest {
        //arrange
        val cardSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(Flashcard(1, "obverse 1", "reverse 1"))
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet)
        sut = TestCardSetViewModel(1, observeCardSetUseCase)

        //act & assert
        sut.uiState.test {
            awaitItem()
            println("ptsz 0 ${awaitItem()}")  // skip first empty emission
            sut.onAnswerProvided("Reverse 1")
            val state = awaitItem()
            println("ptsz 1  $state")
            assertTrue(state is TestCardSetUiState.TestFinished)
            assertEquals(0, (state as TestCardSetUiState.TestFinished).learnedCards)
        }
    }

    @Test
    fun `when in case insensitive mode accept answer in wrong case`() = runTest {
        //arrange
        val cardSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(Flashcard(1, "obverse 1", "reverse 1"))
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet)
        sut = TestCardSetViewModel(1, observeCardSetUseCase)

        //act & assert
        sut.uiState.test {
            skipItems(2)
            sut.onCaseSensitiveChanged(false)
            skipItems(1)
            sut.onAnswerProvided("Reverse 1")
            val state = awaitItem()
            assertTrue(state is TestCardSetUiState.TestFinished)
            assertEquals(1, (state as TestCardSetUiState.TestFinished).learnedCards)
        }
    }

}