package com.szpejsoft.flashcards.domain.usecase.flashcard

import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.repository.FlashcardRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateFlashcardUseCaseTest : BaseMockKTest() {

    private lateinit var sut: UpdateFlashcardUseCase

    @MockK(relaxed = true)
    private lateinit var flashcardRepository: FlashcardRepository


    @Before
    fun setUp() {
        sut = UpdateFlashcardUseCase(flashcardRepository)
    }

    @Test
    fun `when invoke called, repository delete called with proper parameter`() = runTest {
        //arrange
        val flashcardId = 1L
        val obverse = "obverse"
        val reverse = "reverse"
        //act
        sut.invoke(flashcardId, obverse, reverse)
        //assert
        coVerify { flashcardRepository.update(flashcardId, obverse, reverse) }
    }

}