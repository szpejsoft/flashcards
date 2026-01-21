package com.szpejsoft.flashcards.ui.screens.cardsets.learn.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.szpejsoft.flashcards.domain.model.CardSet
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LearnCardSetListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val learnCardSetCalls = mutableListOf<Long>()
    private lateinit var viewModel: LearnCardSetListViewModelTestDouble

    @Before
    fun setUp() {
        viewModel = LearnCardSetListViewModelTestDouble()
        composeTestRule.setContent {
            LearnCardSetListScreen(
                viewModel = viewModel,
                onLearnButtonClick = { id -> learnCardSetCalls.add(id) }
            )
        }
        learnCardSetCalls.clear()
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
        composeTestRule.onNodeWithText("my first cardset", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("my second cardset", ignoreCase = true).assertIsDisplayed()
    }


    @Test
    fun whenUserClicksLearn_callProperLambda() {
        //arrange
        val sets = listOf(
            CardSet(id = 1L, "my first cardset"),
            CardSet(id = 2L, "my second cardset"),
        )

        //act
        viewModel.setCardSets(sets)
        composeTestRule.onNodeWithText("my first cardset").performClick()

        //assert
        assertEquals(listOf(1L), learnCardSetCalls)
    }

}
