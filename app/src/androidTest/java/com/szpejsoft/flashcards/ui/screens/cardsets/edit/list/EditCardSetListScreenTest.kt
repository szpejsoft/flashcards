package com.szpejsoft.flashcards.ui.screens.cardsets.edit.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.domain.model.CardSet
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditCardSetListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val editCardSetCalls = mutableListOf<Long>()
    private val deleteText by lazy { composeTestRule.activity.getString(R.string.action_delete) }
    private val editText by lazy { composeTestRule.activity.getString(R.string.action_edit) }

    private lateinit var viewModel: EditCardSetListViewModelTestDouble

    @Before
    fun setUp() {
        viewModel = EditCardSetListViewModelTestDouble()
        composeTestRule.setContent {
            EditCardSetListScreen(
                viewModel = viewModel,
                onAddButtonClick = {},
                onEditButtonClick = { id -> editCardSetCalls.add(id) },
            )
        }
        editCardSetCalls.clear()
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
    fun whenUserClicksDelete_callProperMethodOnViewModel() {
        //arrange
        val sets = listOf(
            CardSet(id = 1L, "my first cardset"),
            CardSet(id = 2L, "my second cardset"),
        )

        //act
        viewModel.setCardSets(sets)
        composeTestRule.onNodeWithText("my first cardset").performClick()
        composeTestRule.onNodeWithText(deleteText).performClick()

        //assert
        assertEquals(listOf(1L), viewModel.deleteCardSetCalls)
    }

    @Test
    fun whenUserClicksEdit_callProperLambda() {
        //arrange
        val sets = listOf(
            CardSet(id = 1L, "my first cardset"),
            CardSet(id = 2L, "my second cardset"),
        )

        //act
        viewModel.setCardSets(sets)
        composeTestRule.onNodeWithText("my second cardset").performClick()
        composeTestRule.onNodeWithText(editText).performClick()

        //assert
        assertEquals(listOf(2L), editCardSetCalls)
    }



}
