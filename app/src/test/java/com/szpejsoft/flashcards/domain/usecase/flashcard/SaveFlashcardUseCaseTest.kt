package com.szpejsoft.flashcards.domain.usecase.flashcard

import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.repository.FlashcardRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveFlashcardUseCaseTest : BaseMockKTest() {

    private lateinit var sut: SaveFlashcardUseCase

    @MockK(relaxed = true)
    private lateinit var flashcardRepository: FlashcardRepository


    @Before
    fun setUp() {
        sut = SaveFlashcardUseCase(flashcardRepository)
    }

    @Test
    fun `when invoke called, repository delete called with proper parameter`() = runTest {
        //arrange
        val cardSetId = 1L
        val obverse = "obverse"
        val reverse = "reverse"
        //act
        sut.invoke(cardSetId, obverse, reverse)
        //assert
        coVerify { flashcardRepository.save(cardSetId, obverse, reverse) }
    }

}