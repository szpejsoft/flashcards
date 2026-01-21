package com.szpejsoft.flashcards.screens.cardsets.edit.list

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.usecase.cardset.DeleteCardSetUseCase
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.list.EditCardSetListViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditCardSetListViewModelTest : BaseMockKTest() {
    private lateinit var sut: EditCardSetListViewModel

    @MockK(relaxed = true)
    private lateinit var observeCardSetsUseCase: ObserveCardSetsUseCase

    @MockK(relaxed = true)
    private lateinit var deleteCardSetUseCase: DeleteCardSetUseCase

    @Before
    fun setUp() {
        sut = EditCardSetListViewModel(observeCardSetsUseCase, deleteCardSetUseCase)
    }

    @Test
    fun `when usecase shows cardsets, viewmodel shows cardsets`() = runTest {
        //arrange
        val sets = listOf(
            CardSet(1, "set 1"),
            CardSet(2, "set 2")
        )
        val useCaseFlow = flowOf(sets)
        every { observeCardSetsUseCase() } returns useCaseFlow

        sut = EditCardSetListViewModel(observeCardSetsUseCase, deleteCardSetUseCase)

        //act & assert
        sut.uiState.test {
            val sets = expectMostRecentItem().cardSets
            assertEquals(2, sets.size)
            assertEquals(CardSet(1, "set 1"), sets[0])
            assertEquals(CardSet(2, "set 2"), sets[1])
            cancelAndIgnoreRemainingEvents()
        }
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
