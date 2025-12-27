package com.szpejsoft.flashcards.domain.usecase

import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DeleteCardSetUseCaseTest {
    private lateinit var sut: DeleteCardSetUseCase
    @MockK(relaxed = true)
    private lateinit var cardSetRepository: CardSetRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = DeleteCardSetUseCase(cardSetRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
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