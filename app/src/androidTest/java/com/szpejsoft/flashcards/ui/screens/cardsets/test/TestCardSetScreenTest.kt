package com.szpejsoft.flashcards.ui.screens.cardsets.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.model.PracticeMode
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState.FlashcardToTest
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState.TestFinished
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestCardSetScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    val successText by lazy { composeTestRule.activity.getString(R.string.learn_card_set_screen_learning_finished_title) }
    val goToListButton by lazy { composeTestRule.activity.getString(R.string.learn_card_set_screen_learning_go_to_card_set_list) }
    val caseSensitiveText by lazy { composeTestRule.activity.getString(R.string.practice_settings_menu_case_sensitive) }
    val settingsButtonContentDescription by lazy { composeTestRule.activity.getString(R.string.wcag_button_settings_description) }
    val testModeClickName by lazy { composeTestRule.activity.getString(R.string.practice_settings_menu_practice_mode_click) }
    val testModeWriteName by lazy { composeTestRule.activity.getString(R.string.practice_settings_menu_practice_mode_write) }

    private var navigateBackCalls = 0

    private lateinit var viewModel: TestCardSetViewModelTestDouble

    @Before
    fun setUp() {
        viewModel = TestCardSetViewModelTestDouble()
        composeTestRule.setContent {
            TestCardSetScreen(onNavigateBack = { navigateBackCalls++ }, viewModel = viewModel)
        }
        navigateBackCalls = 0
    }

    @Test
    fun whenViewModelEmitsTestFinishedState_screenShowsFinishedState() {
        //arrange

        //act
        viewModel.uiState.value = TestFinished(7, 10)

        //assert
        val resultText = composeTestRule.activity.getString(R.string.test_card_set_screen_test_finished_score, 7, 10)
        composeTestRule.onNodeWithText(successText).assertIsDisplayed()
        composeTestRule.onNodeWithText(goToListButton).assertIsDisplayed()
        composeTestRule.onNodeWithText(resultText).assertIsDisplayed()
    }

    @Test
    fun whenInFinishedStateAndGoToListClicked_navigateBackIsCalled() {
        //arrange
        viewModel.uiState.value = TestFinished(7, 10)

        //act
        composeTestRule.onNodeWithText(goToListButton).performClick()

        //assert
        assertEquals(1, navigateBackCalls)
    }

    @Test
    fun whenViewModelEmitsFlashcardToTestState_screenShowsFlashcardToLearn() {
        //arrange

        //act
        viewModel.uiState.value = FLASHCARD_TO_TEST

        //assert
        composeTestRule.onNodeWithText("setname").assertIsDisplayed()
        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithText("answer").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("learningProgress").assertTextEquals("6/17")
    }

    @Test
    fun flashcardToTestState_whenUserClicksOnCardNotLearned_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST

        //act
        composeTestRule.onNodeWithTag("cardNotLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardNotLearnedCalled)
        assertFalse(viewModel.onCardLearnedCalled)
    }

    @Test
    fun flashcardToTestState_whenUserClicksOnCardLearned_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST

        //act
        composeTestRule.onNodeWithTag("cardLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardLearnedCalled)
        assertFalse(viewModel.onCardNotLearnedCalled)
    }

    @Test
    fun flashcardToTestStateWriteMode_whenUserProvidesAnswer_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST.copy(testMode = PracticeMode.Write)
        composeTestRule.waitForIdle()

        //act
        composeTestRule.onNodeWithTag("answerTextField")
            .apply {
                performTextInput("answer")
                performImeAction()
            }

        //assert
        assertEquals(listOf("answer"), viewModel.onAnswerProvidedCalls)
    }

    @Test
    fun flashcardToTestStateWriteMode_whenClicksOnCardNotLearned_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST.copy(testMode = PracticeMode.Write)

        //act
        composeTestRule.onNodeWithTag("cardNotLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardNotLearnedCalled)
    }

    @Test
    fun whenTestModeClickAndUserClicksSettings_properSettingsAreDisplayed() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST.copy(testMode = PracticeMode.Click)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.waitForIdle()

        //assert
        composeTestRule.onNodeWithTag(testModeClickName, useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testModeClickName, useUnmergedTree = true).assertIsSelected()
        composeTestRule.onNodeWithTag(testModeWriteName, useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testModeWriteName, useUnmergedTree = true).assertIsNotSelected()
        composeTestRule.onNodeWithText(caseSensitiveText).assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch", useUnmergedTree = true).assertIsNotEnabled()
    }

    @Test
    fun whenTestModeClickAndUserClicksChangesMode_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST.copy(testMode = PracticeMode.Click)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.onNodeWithTag(testModeWriteName, useUnmergedTree = true).performClick()

        //assert
        assertEquals(listOf(PracticeMode.Write), viewModel.onPracticeModeChangedCalls)
    }

    @Test
    fun whenTestModeWriteAndUserClicksChangesMode_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST.copy(testMode = PracticeMode.Write)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.onNodeWithTag(testModeClickName, useUnmergedTree = true).performClick()

        //assert
        assertEquals(listOf(PracticeMode.Click), viewModel.onPracticeModeChangedCalls)
    }

    @Test
    fun whenTestModeClickAndCaseInsensitiveSettings_properSettingsAreDisplayed() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST.copy(testMode = PracticeMode.Write, caseSensitive = false)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()

        //assert
        composeTestRule.onNodeWithTag(testModeClickName, useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testModeClickName, useUnmergedTree = true).assertIsNotSelected()
        composeTestRule.onNodeWithTag(testModeWriteName, useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testModeWriteName, useUnmergedTree = true).assertIsSelected()
        composeTestRule.onNodeWithText(caseSensitiveText).assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch", useUnmergedTree = true).assertIsEnabled()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch", useUnmergedTree = true).assertIsOff()
    }

    @Test
    fun whenTestModeClickAndUserWriteCaseSensitiveSettings_properSettingsAreDisplayed() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST.copy(testMode = PracticeMode.Write, caseSensitive = true)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.waitForIdle()

        //assert
        composeTestRule.onNodeWithTag(testModeClickName, useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testModeClickName, useUnmergedTree = true).assertIsNotSelected()
        composeTestRule.onNodeWithTag(testModeWriteName, useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testModeWriteName, useUnmergedTree = true).assertIsSelected()
        composeTestRule.onNodeWithText(caseSensitiveText).assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch", useUnmergedTree = true).assertIsEnabled()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch", useUnmergedTree = true).assertIsOn()
    }


    @Test
    fun whenTestModeClickAndUserWriteCaseSensitiveSettingsAndUserChangesCaseSensitive_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_TEST.copy(testMode = PracticeMode.Write, caseSensitive = true)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch", useUnmergedTree = true).performClick()

        //assert
        assertEquals(listOf(false), viewModel.onCaseSensitiveChangedCalls)
    }

    companion object {
        private val FLASHCARD_TO_TEST = FlashcardToTest(
            setName = "setname",
            cardSetSize = 17,
            learnedCards = 6,
            failedCards = 3,
            flashcardToTest = Flashcard(
                obverse = "question",
                reverse = "answer"
            ),
            testMode = PracticeMode.Click,
            caseSensitive = true
        )
    }

}
