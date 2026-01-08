package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateCardSetUseCaseTest : BaseMockKTest() {
    private lateinit var sut: UpdateCardSetUseCase

    @MockK(relaxed = true)
    private lateinit var cardSetRepository: CardSetRepository

    @Before
    fun setUp() {
        sut = UpdateCardSetUseCase(cardSetRepository)
    }

    @Test
    fun `when invoke called, repository update called with proper parameters`() = runTest {
        //arrange
        val cardSetId = 1L
        val cardSetName = "name"

        //act
        sut.invoke(cardSetId, cardSetName)

        //assert
        coVerify(exactly = 1) { cardSetRepository.update(cardSetId, cardSetName) }
    }


}