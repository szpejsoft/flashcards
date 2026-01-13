package com.szpejsoft.flashcards.screens.cardsets.add

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.usecase.cardset.SaveCardSetUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.add.AddCardSetUiState
import com.szpejsoft.flashcards.ui.screens.cardsets.add.AddCardSetViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddCardSetViewModelTest : BaseMockKTest() {

    private lateinit var sut: AddCardSetViewModel

    @MockK(relaxed = true)
    private lateinit var saveCardSetUseCase: SaveCardSetUseCase

    @Before
    fun setUp() {
        sut = AddCardSetViewModel(saveCardSetUseCase)
    }

    @Test
    fun `when name changed to non-blank, state is editing with save enabled`() {
        //arrange
        val setName = "testSet"

        //act
        sut.onCardSetNameChanged(setName)

        //assert
        val state = sut.uiState.value
        assertTrue(state is AddCardSetUiState.Editing)
        assertTrue((state as AddCardSetUiState.Editing).isSaveEnabled)
        assertEquals(setName, state.name)
    }


    @Test
    fun `when name changed to blank, state is editing with save disabled`() {
        //arrange
        val setName = "testSet"
        sut.onCardSetNameChanged(setName)
        //act
        sut.onCardSetNameChanged("")

        //assert
        val state = sut.uiState.value
        assertTrue(state is AddCardSetUiState.Editing)
        assertFalse((state as AddCardSetUiState.Editing).isSaveEnabled)
        assertEquals("", state.name)
    }

    @Test
    fun `when save clicked, usecase called with proper parameters`() = runTest {
        //arrange
        val setName = "testSet"
        sut.onCardSetNameChanged(setName)
        //act
        sut.onSaveClicked()

        //assert
        coVerify(exactly = 1) { saveCardSetUseCase(setName) }
    }

    @Test
    fun `when save clicked, ui state transitions from Editing through Saving to Saved`() = runTest {
        //arrange
        val setName = "testSet"
        sut.onCardSetNameChanged(setName)
        //act & assert
        sut.uiState.test {
            //initial state is Editing (specific fields tested in other tests)
            assertTrue(awaitItem() is AddCardSetUiState.Editing)

            sut.onSaveClicked()
            //state changes to saving
            assertEquals(AddCardSetUiState.Saving, awaitItem())
            //state changes to saved
            assertEquals(AddCardSetUiState.Saved, awaitItem())
        }
    }

    @Test
    fun `when save fails, ui state transitions from Editing through Saving to Error`() = runTest {
        //arrange
        val setName = "testSet"
        val exception = RuntimeException("test exception")
        sut.onCardSetNameChanged(setName)
        coEvery { saveCardSetUseCase(setName) } throws exception

        //act & assert
        sut.uiState.test {
            //initial Editing state
            awaitItem()

            sut.onSaveClicked()
            //Saving state
            awaitItem()

            //state changes to saved
            val errorState = awaitItem()
            assertTrue(errorState is AddCardSetUiState.Error)
            assertEquals(exception.message, (errorState as AddCardSetUiState.Error).message)
        }
    }

}
