package com.szpejsoft.flashcards.screens.cardsets.learn.list

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.list.LearnCardSetListViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LearnCardSetListViewModelTest : BaseMockKTest() {
    private lateinit var sut: LearnCardSetListViewModel

    @MockK(relaxed = true)
    private lateinit var observeCardSetsUseCase: ObserveCardSetsUseCase

    @Before
    fun setUp() {
        sut = LearnCardSetListViewModel(observeCardSetsUseCase)
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

        sut = LearnCardSetListViewModel(observeCardSetsUseCase)

        //act & assert
        sut.uiState.test {
            skipItems(1)
            val sets = awaitItem().cardSets
            assertEquals(2, sets.size)
            assertEquals(CardSet(1, "set 1"), sets[0])
            assertEquals(CardSet(2, "set 2"), sets[1])
            cancelAndIgnoreRemainingEvents()
        }
    }


}
