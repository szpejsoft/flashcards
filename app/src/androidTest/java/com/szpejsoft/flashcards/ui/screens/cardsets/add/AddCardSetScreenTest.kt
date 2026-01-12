package com.szpejsoft.flashcards.ui.screens.cardsets.add

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.szpejsoft.flashcards.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddCardSetScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    val textFieldLabel by lazy { composeTestRule.activity.getString(R.string.add_card_set_screen_card_set_title_hint) }

    val saveButtonText by lazy { composeTestRule.activity.getString(R.string.action_save) }

    private lateinit var viewModel: AddCardSetViewModelTestDouble

    @Before
    fun setUp() {
        viewModel = AddCardSetViewModelTestDouble()
        composeTestRule.setContent {
            AddCardSetScreen(viewModel = viewModel) {}
        }
    }

    @Test
    fun whenUserEntersName_properViewModelMethodIsCalled() {
        //arrange
        val cardSetName = "my cardset"

        //act
        composeTestRule.onNodeWithText(textFieldLabel).performTextInput(cardSetName)

        //assert
        assertEquals(1, viewModel.onCardSetNameChangedCalls.size)
        assertEquals(cardSetName, viewModel.onCardSetNameChangedCalls[0])
    }

    @Test
    fun whenUserClicksSave_properViewModelMethodIsCalled() {
        //arrange
        viewModel.setUiState(AddCardSetUiState.Editing("my cardset", true))

        //act
        composeTestRule.onNodeWithText(saveButtonText).performClick()

        //assert
        assertEquals(1, viewModel.onSaveClickedCounter)
    }

}