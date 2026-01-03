package com.szpejsoft.flashcards.domain.usecase.flashcard

import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.repository.FlashcardRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteFlashcardUseCaseTest : BaseMockKTest() {

    private lateinit var sut: DeleteFlashcardUseCase

    @MockK(relaxed = true)
    private lateinit var flashcardRepository: FlashcardRepository


    @Before
    fun setUp() {
        sut = DeleteFlashcardUseCase(flashcardRepository)
    }

    @Test
    fun `when invoke called, repository delete called with proper parameter`() = runTest {
        //arrange

        //act
        sut.invoke(1)
        //assert
        coVerify { flashcardRepository.delete(1) }

    }

}