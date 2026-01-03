package com.szpejsoft.flashcards.screens.cardset.list

import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.usecase.cardset.DeleteCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.list.CardSetListUiState
import com.szpejsoft.flashcards.ui.screens.cardsets.list.CardSetListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CardSetListViewModelTest {
    private lateinit var sut: CardSetListViewModel

    @MockK(relaxed = true)
    private lateinit var observeCardSetsUseCase: ObserveCardSetsUseCase

    @MockK(relaxed = true)
    private lateinit var deleteCardSetUseCase: DeleteCardSetUseCase


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = CardSetListViewModel(observeCardSetsUseCase, deleteCardSetUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when usecase shows cardsets, viewmodel shows cardsets`() = runTest {
        //arrange
        val sets = listOf(
            CardSet(1, "set 1"),
            CardSet(2, "set 2")
        )
        val stateFlow = MutableStateFlow<List<CardSet>>(emptyList())
        every { observeCardSetsUseCase() } returns stateFlow

        //act
        stateFlow.value = sets

        //assert
        val uiState = (sut.cardSets.value as CardSetListUiState.Idle).cardSets
        assertEquals(2, uiState.size)
        assertEquals(CardSet(1, "set 1"), uiState[0])
        assertEquals(CardSet(2, "set 2"), uiState[1])
    }

    @Test
    fun `when delete clicked, deleteCardSetUseCase called with proper parameters`() = runTest {
        //arrange
        val setToDeleteId = 17L

        //act
        sut.onDeleteCardSetClicked(setToDeleteId)

        //assert
        coVerify(exactly = 1) { deleteCardSetUseCase(setToDeleteId) }
    }


}