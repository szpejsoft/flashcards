package com.szpejsoft.flashcards.domain.usecase.cardset

import com.szpejsoft.flashcards.common.BaseMockKTest
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveCardSetUseCaseTest : BaseMockKTest() {
    private lateinit var sut: SaveCardSetUseCase

    @MockK(relaxed = true)
    private lateinit var cardSetRepository: CardSetWithFlashcardsRepository

    @Before
    fun setUp() {
        sut = SaveCardSetUseCase(cardSetRepository)
    }

    @Test
    fun `when invoke called, repository save called with proper parameter`() = runTest {
        //arrange
        val cardSetName = "name"
        val flashcards = listOf(
            Flashcard(0, "obverse", "reverse")
        )

        //act
        sut.invoke(cardSetName, flashcards)

        //assert
        coVerify(exactly = 1) { cardSetRepository.insert(cardSetName, flashcards) }
    }

}
