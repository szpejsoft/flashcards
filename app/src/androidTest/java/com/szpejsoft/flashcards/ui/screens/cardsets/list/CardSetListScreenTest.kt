package com.szpejsoft.flashcards.ui.screens.cardsets.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.szpejsoft.flashcards.domain.model.CardSet
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CardSetListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var viewModel: CardSetListViewModel

    @Before
    fun setUp() {
        viewModel = CardSetListViewModel()
        composeTestRule.setContent {
            viewModel = CardSetListViewModel()
            CardSetListScreen(
                viewModel = viewModel,
                onAddButtonClick = {}
            )
        }
    }

    @Test
    fun whenViewModelProvidesCardSets_showThem() {
        //arrange
        val sets = listOf(
            CardSet(id = 1L, "my first cardset"),
            CardSet(id = 2L, "my second cardset"),
        )

        //act
        viewModel.setCardSets(sets)

        //assert
        composeTestRule.onNodeWithText("my first cardset").assertIsDisplayed()
        composeTestRule.onNodeWithText("my second cardset").assertIsDisplayed()
    }

    @Test
    fun whenUserClicksDelete_callProperMethodOnViewModel() {
        //arrange
        val sets = listOf(
            CardSet(id = 1L, "my first cardset"),
            CardSet(id = 2L, "my second cardset"),
        )

        //act
        viewModel.setCardSets(sets)
        composeTestRule.onNodeWithText("my first cardset").performClick()
        composeTestRule.onNodeWithText("Delete").performClick()

        //assert
        assertEquals(listOf(1L), viewModel.deleteCardSetCalls)
    }

}