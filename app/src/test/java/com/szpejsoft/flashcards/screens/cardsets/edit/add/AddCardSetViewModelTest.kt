package com.szpejsoft.flashcards.screens.cardsets.edit.add

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseTest
import com.szpejsoft.flashcards.domain.usecase.cardset.SaveCardSetUseCase
import com.szpejsoft.flashcards.presentation.cardsets.AddCardSetViewModelImpl
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddCardSetViewModelTest : BaseTest() {

    private lateinit var sut: AddCardSetViewModelImpl

    @MockK(relaxed = true)
    private lateinit var saveCardSetUseCase: SaveCardSetUseCase

    @Before
    fun setUp() {
        sut = AddCardSetViewModelImpl(saveCardSetUseCase)
    }

    @Test
    fun `when viewmodel created, it emits empty cardset`() = runTest {
        //arrange

        //act

        //assert
        sut.uiState.test {
            val state = awaitItem()
            assertEquals("", state.setName)
            assertTrue(state.flashCards.isEmpty())
            assertFalse(state.saveEnabled)
        }
    }

    @Test
    fun `when user enters setName, viewmodel emits proper state`() = runTest {
        //arrange

        //act

        //assert
        sut.uiState.test {
            awaitItem()
            sut.onCardSetNameChanged("name")

            val updatedState = awaitItem()
            assertEquals("name", updatedState.setName)
            assertFalse(updatedState.saveEnabled)
        }
    }

    @Test
    fun `when user enters setName and flashcard, viewmodel emits proper state`() = runTest {
        //arrange

        //act & assert
        sut.uiState.test {
            awaitItem()
            sut.onCardSetNameChanged("name")
            awaitItem()
            sut.onAddFlashcard("obverse", "reverse")

            val updatedState = awaitItem()
            assertEquals("name", updatedState.setName)
            assertTrue(updatedState.saveEnabled)
            assertEquals("obverse", updatedState.flashCards[0].obverse)
            assertEquals("reverse", updatedState.flashCards[0].reverse)
        }
    }

    @Test
    fun `when user deletes flashcard, viewmodel emits proper state`() = runTest {
        //arrange

        //act & assert
        sut.uiState.test {
            awaitItem()
            sut.onCardSetNameChanged("name")
            awaitItem()
            sut.onAddFlashcard("obverse", "reverse")
            awaitItem()
            sut.onDeleteFlashcard(1)  //1 is the id of the flashcard we just added
            val updatedState = awaitItem()
            assertEquals("name", updatedState.setName)
            assertTrue(updatedState.flashCards.isEmpty())
        }
    }

    @Test
    fun `when user updates flashcard, viewmodel emits proper state`() = runTest {
        //arrange

        //act & assert
        sut.uiState.test {
            awaitItem()
            sut.onCardSetNameChanged("name")
            awaitItem()
            sut.onAddFlashcard("obverse", "reverse")
            awaitItem()
            sut.onUpdateFlashcard(1, "o", "r")  //1 is the id of the flashcard we just added
            val updatedState = awaitItem()
            assertEquals("o", updatedState.flashCards[0].obverse)
            assertEquals("r", updatedState.flashCards[0].reverse)
        }
    }



}
