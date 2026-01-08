package com.szpejsoft.flashcards.screens.cardsets.edit

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.cardset.UpdateCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.flashcard.DeleteFlashcardUseCase
import com.szpejsoft.flashcards.domain.usecase.flashcard.SaveFlashcardUseCase
import com.szpejsoft.flashcards.domain.usecase.flashcard.UpdateFlashcardUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetUiState
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EditCardSetViewModelTest : BaseMockKTest() {
    private lateinit var sut: EditCardSetViewModel

    @MockK(relaxed = true)
    private lateinit var deleteFlashcardUseCase: DeleteFlashcardUseCase

    @MockK(relaxed = true)
    private lateinit var observeCardSetUseCase: ObserveCardSetUseCase

    @MockK(relaxed = true)
    private lateinit var saveFlashcardUseCase: SaveFlashcardUseCase

    @MockK(relaxed = true)
    private lateinit var updateFlashcardUseCase: UpdateFlashcardUseCase

    @MockK(relaxed = true)
    private lateinit var updateCardSetUseCase: UpdateCardSetUseCase

    @Before
    fun setUp() {
        sut = EditCardSetViewModel(
            CARD_SET_ID,
            deleteFlashcardUseCase,
            observeCardSetUseCase,
            saveFlashcardUseCase,
            updateFlashcardUseCase,
            updateCardSetUseCase
        )
    }

    @Suppress("UnusedFlow")
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when viewmodel created, it observes proper cardSet`() = runTest {
        //arrange

        //act

        //assert
        coVerify(exactly = 1) { observeCardSetUseCase(CARD_SET_ID) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when usecase emits cardSet, viewmodel emits Idle state with proper cardSet`() = runTest {
        //arrange
        val cardSet = CardSetWithFlashcards(
            cardSet = CardSet(CARD_SET_ID, "card set name"),
            flashcards = listOf(
                Flashcard(1, "obverse 1", "reverse 1"),
                Flashcard(2, "obverse 2", "reverse 2")
            )
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet)
        sut = EditCardSetViewModel(
            CARD_SET_ID,
            deleteFlashcardUseCase,
            observeCardSetUseCase,
            saveFlashcardUseCase,
            updateFlashcardUseCase,
            updateCardSetUseCase
        )

        //act & assert
        sut.uiState.test {
            assertEquals(EditCardSetUiState.Idle(), awaitItem())
            advanceUntilIdle()
            val state = awaitItem()
            assertTrue(state is EditCardSetUiState.Idle)
            val flashcards = (state as EditCardSetUiState.Idle).flashCards
            assertEquals("card set name", state.cardSetName)
            assertEquals(2, flashcards.size)
            assertEquals(Flashcard(1, "obverse 1", "reverse 1"), flashcards[0])
            assertEquals(Flashcard(2, "obverse 2", "reverse 2"), flashcards[1])

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when onAddFlashcard is called, proper useCase is called with proper parameters`() = runTest {
        //arrange

        //act
        sut.onAddFlashcard(OBVERSE, REVERSE)

        //assert
        coVerify(exactly = 1) { saveFlashcardUseCase(CARD_SET_ID, OBVERSE, REVERSE) }
    }

    @Test
    fun `when onDeleteFlashcard is called, proper useCase is called with proper parameters`() = runTest {
        //arrange
        val flashcardId = 42L

        //act
        sut.onDeleteFlashcard(flashcardId)

        //assert
        coVerify(exactly = 1) { deleteFlashcardUseCase(flashcardId) }
    }

    @Test
    fun `when onUpdateFlashcard is called, proper useCase is called with proper parameters`() = runTest {
        //arrange

        //act
        sut.onUpdateFlashcard(FLASHCARD_ID, OBVERSE, REVERSE)

        //assert
        coVerify(exactly = 1) { updateFlashcardUseCase(FLASHCARD_ID, OBVERSE, REVERSE) }
    }

    @Test
    fun `when onUpdateCardSetName is called, proper useCase is called with proper parameters`() = runTest {
        //arrange

        //act
        sut.onUpdateCardSetName("new name")

        //assert
        coVerify(exactly = 1) { updateCardSetUseCase(CARD_SET_ID, "new name") }
    }


    companion object {
        private const val CARD_SET_ID = 1L
        private const val FLASHCARD_ID = 42L
        private const val OBVERSE = "obverse"
        private const val REVERSE = "reverse"
    }

}