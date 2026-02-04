package com.szpejsoft.flashcards.presentation.test

import app.cash.turbine.test
import com.szpejsoft.flashcards.common.BaseTest
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.domain.usecase.cardset.ObserveCardSetsUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestCardSetListViewModelTest : BaseTest() {
    private lateinit var sut: TestCardSetListViewModelImpl

    @MockK(relaxed = true)
    private lateinit var observeCardSetsUseCase: ObserveCardSetsUseCase

    @Before
    fun setUp() {
        sut = TestCardSetListViewModelImpl(observeCardSetsUseCase)
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

        sut = TestCardSetListViewModelImpl(observeCardSetsUseCase)

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