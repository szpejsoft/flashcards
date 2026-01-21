package com.szpejsoft.flashcards.ui.screens.cardsets.test.list

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

class TestCardSetListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testCardSetCalls = mutableListOf<Long>()

    private lateinit var viewModel: TestCardSetListViewModelTestDouble

    @Before
    fun setUp() {
        viewModel = TestCardSetListViewModelTestDouble()
        composeTestRule.setContent {
            TestCardSetListScreen(
                viewModel = viewModel,
                onTestButtonClick = { id -> testCardSetCalls.add(id) }
            )
        }
        testCardSetCalls.clear()
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
    fun whenUserClicksTest_callProperLambda() {
        //arrange
        val sets = listOf(
            CardSet(id = 1L, "my first cardset"),
            CardSet(id = 2L, "my second cardset"),
        )

        //act
        viewModel.setCardSets(sets)
        composeTestRule.onNodeWithText("my second cardset").performClick()

        //assert
        assertEquals(listOf(2L), testCardSetCalls)
    }

}
