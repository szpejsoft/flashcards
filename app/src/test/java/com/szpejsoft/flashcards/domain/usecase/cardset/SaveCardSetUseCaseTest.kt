package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveCardSetUseCaseTest : BaseMockKTest() {
    private lateinit var sut: SaveCardSetUseCase

    @MockK(relaxed = true)
    private lateinit var cardSetRepository: CardSetRepository

    @Before
    fun setUp() {
        sut = SaveCardSetUseCase(cardSetRepository)
    }

    @Test
    fun `when invoke called, repository save called with proper parameter`() = runTest {
        //arrange
        val cardSetName = "name"

        //act
        sut.invoke(cardSetName)

        //assert
        coVerify(exactly = 1) { cardSetRepository.save(cardSetName) }
    }

}
