package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateCardSetUseCaseTest : BaseMockKTest() {
    private lateinit var sut: UpdateCardSetUseCase

    @MockK(relaxed = true)
    private lateinit var cardSetRepository: CardSetWithFlashcardsRepository

    @Before
    fun setUp() {
        sut = UpdateCardSetUseCase(cardSetRepository)
    }

    @Test
    fun `when invoke called, repository update called with proper parameters`() = runTest {
        //arrange
        val cardSetId = 1L
        val cardSetName = "name"
        val flashcards = emptyList<Flashcard>()
        val flashcardIdsToDelete = listOf(1L)


        //act
        sut.invoke(cardSetId, cardSetName, flashcards, flashcardIdsToDelete)

        //assert
        coVerify(exactly = 1) { cardSetRepository.update(cardSetId, cardSetName, flashcards, flashcardIdsToDelete) }
    }


}