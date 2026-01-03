package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteCardSetUseCaseTest: BaseMockKTest() {
    private lateinit var sut: DeleteCardSetUseCase

    @MockK(relaxed = true)
    private lateinit var cardSetRepository: CardSetRepository

    @Before
    fun setUp() {
        sut = DeleteCardSetUseCase(cardSetRepository)
    }

    @Test
    fun `when invoke called, repository delete called with proper parameter`() = runTest {
        //arrange

        //act
        sut.invoke(1)
        //assert
        coVerify { cardSetRepository.delete(1) }
    }

}