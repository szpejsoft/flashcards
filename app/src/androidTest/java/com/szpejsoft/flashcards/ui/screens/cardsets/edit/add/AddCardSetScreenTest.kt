package com.szpejsoft.flashcards.ui.screens.cardsets.edit.add

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.presentation.cardsets.AddCardSetViewModel.UiState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddCardSetScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    val titleTextFieldLabel by lazy { composeTestRule.activity.getString(R.string.add_card_set_screen_card_set_title_hint) }
    private val actionAdd by lazy { composeTestRule.activity.getString(R.string.action_add) }
    private val actionCancel by lazy { composeTestRule.activity.getString(R.string.action_cancel) }
    private val actionDelete by lazy { composeTestRule.activity.getString(R.string.action_delete) }
    private val actionEdit by lazy { composeTestRule.activity.getString(R.string.action_edit) }
    private val actionSave by lazy { composeTestRule.activity.getString(R.string.action_save) }
    private val actionUpdate by lazy { composeTestRule.activity.getString(R.string.action_update) }
    private val addContentDescription by lazy { composeTestRule.activity.getString(R.string.wcag_action_add) }
    private val addFlashcardText by lazy { composeTestRule.activity.getString(R.string.edit_card_set_screen_add_flashcard_dialog_title) }
    private val updateFlashcardText by lazy { composeTestRule.activity.getString(R.string.edit_card_set_screen_update_flashcard_dialog_title) }
    private val obverseDialogText by lazy { composeTestRule.activity.getString(R.string.edit_card_set_screen_edit_flashcard_dialog_obverse_label) }
    private val reverseDialogText by lazy { composeTestRule.activity.getString(R.string.edit_card_set_screen_edit_flashcard_dialog_reverse_label) }

    private lateinit var viewModel: AddCardSetViewModelTestDouble

    private var onNavigateBackCalls = 0

    @Before
    fun setUp() {
        viewModel = AddCardSetViewModelTestDouble()
        composeTestRule.setContent {
            AddCardSetScreen(viewModel = viewModel, onNavigateBack = { onNavigateBackCalls++ })
        }
    }

    @Test
    fun whenUserEntersSetName_properViewModelMethodIsCalled() {
        //arrange
        val cardSetName = "my cardset"

        //act
        composeTestRule.onNodeWithText(titleTextFieldLabel).apply {
            performTextInput(cardSetName)
            performImeAction()
        }

        //assert
        assertEquals(1, viewModel.onCardSetNameChangedCalls.size)
        assertEquals(cardSetName, viewModel.onCardSetNameChangedCalls[0])
    }

    @Test
    fun whenViewModelEmitsCardSet_screenShowsCardSet() {
        //arrange
        val uiState = UiState(
            "set name",
            listOf(
                Flashcard(1L, "obverse_1", "reverse_1"),
                Flashcard(2L, "obverse_2", "reverse_2")
            )
        )

        //act
        viewModel.setUiState(uiState)

        //assert
        composeTestRule.onNodeWithText("set name").assertIsDisplayed()
        composeTestRule.onNodeWithText("obverse_1").assertIsDisplayed()
        composeTestRule.onNodeWithText("obverse_2").assertIsDisplayed()
        composeTestRule.onNodeWithText("reverse_1").assertIsDisplayed()
        composeTestRule.onNodeWithText("reverse_2").assertIsDisplayed()
    }

    @Test
    fun whenDeleteFlashCardIsClicked_properViewModelMethodIsCalled() {
        //arrange
        val uiState = UiState(
            "set name",
            listOf(
                Flashcard(1L, "obverse_1", "reverse_1"),
                Flashcard(2L, "obverse_2", "reverse_2")
            )
        )

        //act
        viewModel.setUiState(uiState)
        composeTestRule.onNodeWithText("obverse_1").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(actionDelete).performClick()

        //assert
        assertEquals(1, viewModel.onDeleteFlashcardCalls.size)
        assertEquals(1L, viewModel.onDeleteFlashcardCalls[0])
    }

    @Test
    fun whenFABIsClicked_addFlashcardDialogIsShown() {
        //arrange
        val uiState = UiState("set name")
        viewModel.setUiState(uiState)

        //act
        composeTestRule.onNodeWithContentDescription(addContentDescription).performClick()
        composeTestRule.waitForIdle()
        //assert
        composeTestRule.onNodeWithText(addFlashcardText).assertIsDisplayed()
    }

    @Test
    fun whenAddCardIsClicked_properViewModelMethodIsCalled() {
        //arrange
        val uiState = UiState("set name")
        viewModel.setUiState(uiState)
        composeTestRule.onNodeWithContentDescription(addContentDescription).performClick()
        composeTestRule.waitForIdle()

        //act
        composeTestRule.onNodeWithText(obverseDialogText).performTextInput("obverse1")
        composeTestRule.onNodeWithText(reverseDialogText).performTextInput("reverse1")
        composeTestRule.onNodeWithText(actionAdd).performClick()

        //assert
        assertEquals(1, viewModel.onAddFlashcardCalls.size)
        assertEquals("obverse1" to "reverse1", viewModel.onAddFlashcardCalls[0])
    }

    @Test
    fun whenCancelClickedWhileCardIsClicked_noViewModelMethodIsCalled() {
        //arrange
        val uiState = UiState("set name")
        viewModel.setUiState(uiState)
        composeTestRule.onNodeWithContentDescription(addContentDescription).performClick()
        composeTestRule.waitForIdle()

        //act
        composeTestRule.onNodeWithText(obverseDialogText).performTextInput("obverse1")
        composeTestRule.onNodeWithText(reverseDialogText).performTextInput("reverse1")
        composeTestRule.onNodeWithText(actionCancel).performClick()

        //assert
        assertEquals(0, viewModel.onAddFlashcardCalls.size)
    }

    @Test
    fun whenEditCardIsClicked_properDialogIsShown() {
        //arrange
        val uiState = UiState(
            "set name",
            listOf(
                Flashcard(1L, "obverse_1", "reverse_1"),
                Flashcard(2L, "obverse_2", "reverse_2")
            )
        )
        viewModel.setUiState(uiState)

        //act
        composeTestRule.onNodeWithText("obverse_1").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(actionEdit).performClick()
        composeTestRule.waitForIdle()

        //assert
        composeTestRule.onNodeWithText(updateFlashcardText).assertIsDisplayed()
    }

    @Test
    fun whenUpdateClickedWhileEditingCard_properViewModelMethodIsCalled() {
        //arrange
        val uiState = UiState(
            "set name",
            listOf(
                Flashcard(1L, "obverse_1", "reverse_1"),
                Flashcard(2L, "obverse_2", "reverse_2")
            )
        )
        viewModel.setUiState(uiState)

        //act
        composeTestRule.onNodeWithText("obverse_1").performClick()
        composeTestRule.onNodeWithText(actionEdit).performClick()
        composeTestRule.onNodeWithTag("update_dialog_obverse_field").apply {
            performTextClearance()
            performTextInput("obverse1")
        }
        composeTestRule.onNodeWithText(actionUpdate).performClick()

        //assert
        assertEquals(1, viewModel.onUpdateFlashcardCalls.size)
        assertEquals(Triple(1L, "obverse1", "reverse_1"), viewModel.onUpdateFlashcardCalls[0])
    }

    @Test
    fun whenCancelClickedWhileEditingCard_noViewModelMethodIsCalled() {
        //arrange
        val uiState = UiState(
            "set name",
            listOf(
                Flashcard(1L, "obverse_1", "reverse_1"),
                Flashcard(2L, "obverse_2", "reverse_2")
            )
        )
        viewModel.setUiState(uiState)

        //act
        composeTestRule.onNodeWithText("obverse_1").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(actionEdit).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("update_dialog_obverse_field").performTextInput("obverse1")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(actionCancel).performClick()

        //assert
        assertEquals(0, viewModel.onUpdateFlashcardCalls.size)
    }

    @Test
    fun whenUserClicksSave_properViewModelMethodIsCalled() {
        //arrange
        viewModel.setUiState(UiState("setName", saveEnabled = true))

        //act
        composeTestRule.onNodeWithText(actionSave).performClick()

        //assert
        assertEquals(1, viewModel.onSaveClickedCounter)
    }

    @Test
    fun whenSaveButtonClicked_returnToPreviousScreen() {
        //arrange
        val uiState = UiState(
            "set name",
            listOf(
                Flashcard(1L, "obverse_1", "reverse_1"),
                Flashcard(2L, "obverse_2", "reverse_2")
            ),
            saveEnabled = true,
        )

        //act
        viewModel.setUiState(uiState)
        composeTestRule.onNodeWithText(actionSave).performClick()

        //assert
        assertEquals(1, onNavigateBackCalls)
    }

}
