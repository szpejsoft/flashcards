package com.szpejsoft.flashcards.screens.cardsets.edit.edit

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.model.CardSetWithFlashcards
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.cardset.UpdateCardSetUseCase
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetViewModelImpl
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EditCardSetViewModelTest : BaseMockKTest() {
    private lateinit var sut: EditCardSetViewModelImpl

    @MockK(relaxed = true)
    private lateinit var observeCardSetUseCase: ObserveCardSetUseCase

    @MockK(relaxed = true)
    private lateinit var updateCardSetUseCase: UpdateCardSetUseCase

    @Before
    fun setUp() {
        sut = EditCardSetViewModelImpl(
            1,
            observeCardSetUseCase,
            updateCardSetUseCase
        )
    }

    @Suppress("UnusedFlow")
    @Test
    fun `when viewmodel created, it observes proper cardSet`() = runTest {
        //arrange

        //act

        //assert
        coVerify(exactly = 1) { observeCardSetUseCase(1) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when usecase emits cardSet, viewmodel emits state with proper cardSet`() = runTest {
        //arrange
        val cardSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(
                Flashcard(1, "obverse 1", "reverse 1"),
                Flashcard(2, "obverse 2", "reverse 2")
            )
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet)
        sut = EditCardSetViewModelImpl(
            1,
            observeCardSetUseCase,
            updateCardSetUseCase
        )

        //act & assert
        sut.uiState.test {
            awaitItem()
            val state = awaitItem()
            val flashcards = state.flashCards
            assertEquals("card set name", state.setName)
            assertEquals(2, flashcards.size)
            assertEquals(Flashcard(1, "obverse 1", "reverse 1"), flashcards[0])
            assertEquals(Flashcard(2, "obverse 2", "reverse 2"), flashcards[1])

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when save clicked proper useCase is called`() = runTest {
        //arrange
        val cardSet = CardSetWithFlashcards(
            cardSet = CardSet(1, "card set name"),
            flashcards = listOf(
                Flashcard(1, "obverse 1", "reverse 1"),
                Flashcard(2, "obverse 2", "reverse 2")
            )
        )
        every { observeCardSetUseCase(1) } returns flowOf(cardSet)
        sut = EditCardSetViewModelImpl(
            1,
            observeCardSetUseCase,
            updateCardSetUseCase
        )

        //act & assert
        sut.uiState.test {
            advanceUntilIdle()
            sut.onUpdateCardSetName("new card set name")
            advanceUntilIdle()
            sut.onDeleteFlashcard(1)
            advanceUntilIdle()
            sut.onUpdateFlashcard(2, "obverse 2_1", "reverse 2_1")
            advanceUntilIdle()
            sut.onAddFlashcard("obverse 3", "reverse 3")
            advanceUntilIdle()
            sut.onSaveClicked()
            advanceUntilIdle()
            val flashcardsSlot = slot<List<Flashcard>>()
            val cardSetIdSlot = slot<Long>()
            val cardSetNameSlot = slot<String>()
            val toDeleteSlot = slot<List<Long>>()
            coVerify(exactly = 1) {
                updateCardSetUseCase(
                    cardSetId = capture(cardSetIdSlot),
                    cardSetName = capture(cardSetNameSlot),
                    flashcardsToSave = capture(flashcardsSlot),
                    flashcardIdsToDelete = capture(toDeleteSlot),
                )
            }
            cancelAndIgnoreRemainingEvents()
            val capturedFlashcards = flashcardsSlot.captured
            assertEquals(2, capturedFlashcards.size)

            val editedFlashcard = capturedFlashcards.first { it.id == 2L }
            assertEquals("obverse 2_1", editedFlashcard.obverse)
            assertEquals("reverse 2_1", editedFlashcard.reverse)

            val newFlashcard = capturedFlashcards.first { it.id == 0L }
            assertEquals("obverse 3", newFlashcard.obverse)
            assertEquals("reverse 3", newFlashcard.reverse)
        }

    }

}
