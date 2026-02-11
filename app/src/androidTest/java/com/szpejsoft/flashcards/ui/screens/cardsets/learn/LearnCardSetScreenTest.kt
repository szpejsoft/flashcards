package com.szpejsoft.flashcards.ui.screens.cardsets.learn

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
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.FlashcardToLearn
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.LearningFinished
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.WrongAnswer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LearnCardSetScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    val successText by lazy { composeTestRule.activity.getString(R.string.learn_card_set_screen_learning_finished_title) }
    val goToListButton by lazy { composeTestRule.activity.getString(R.string.learn_card_set_screen_learning_go_to_card_set_list) }
    val caseSensitiveText by lazy { composeTestRule.activity.getString(R.string.practice_settings_menu_case_sensitive) }
    val settingsButtonContentDescription by lazy { composeTestRule.activity.getString(R.string.wcag_button_settings_description) }
    val learnModeClickName by lazy { composeTestRule.activity.getString(R.string.practice_settings_menu_practice_mode_click) }
    val learnModeWriteName by lazy { composeTestRule.activity.getString(R.string.practice_settings_menu_practice_mode_write) }

    private var navigateBackCalls = 0

    private lateinit var viewModel: LearnCardSetViewModelTestDouble

    @Before
    fun setUp() {
        viewModel = LearnCardSetViewModelTestDouble()
        composeTestRule.setContent {
            LearnCardSetScreen(viewModel = viewModel, onNavigateBack = { navigateBackCalls++ })
        }
        navigateBackCalls = 0
    }

    @Test
    fun whenViewModelEmitsLearningFinishedState_screenShowsFinishedState() {
        //arrange

        //act
        viewModel.uiState.value = LearningFinished

        //assert
        composeTestRule.onNodeWithText(successText).assertIsDisplayed()
        composeTestRule.onNodeWithText(goToListButton).assertIsDisplayed()
    }

    @Test
    fun whenInFinishedStateAndGoToListClicked_navigateBackIsCalled() {
        //arrange
        viewModel.uiState.value = LearningFinished

        //act
        composeTestRule.onNodeWithText(goToListButton).performClick()

        //assert
        assertEquals(1, navigateBackCalls)
    }


    @Test
    fun whenViewModelEmitsFlashcardToLearnState_screenShowsFlashcardToLearn() {
        //arrange

        //act
        viewModel.uiState.value = FLASHCARD_TO_LEARN

        //assert
        composeTestRule.onNodeWithText("setname").assertIsDisplayed()
        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithText("answer").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("learningProgress").assertTextEquals("6/17")
    }

    @Test
    fun flashcardToLearnState_whenUserClicksQuestion_answerIsDisplayed() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_LEARN
        composeTestRule.waitForIdle()

        //act
        composeTestRule.onNodeWithText("question").performClick()
        composeTestRule.waitForIdle()

        //assert
        composeTestRule.onNodeWithText("question").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("answer").assertIsDisplayed()
    }

    @Test
    fun flashcardToLearnState_whenUserClicksOnCardNotLearned_properMethodIsCalled() {
        //arrange

        //act
        viewModel.uiState.value = FLASHCARD_TO_LEARN
        composeTestRule.onNodeWithTag("cardNotLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardNotLearnedCalled)
        assertFalse(viewModel.onCardLearnedCalled)
    }

    @Test
    fun flashcardToLearnState_whenUserClicksOnCardLearned_properMethodIsCalled() {
        //arrange

        //act
        viewModel.uiState.value = FLASHCARD_TO_LEARN
        composeTestRule.onNodeWithTag("cardLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardLearnedCalled)
        assertFalse(viewModel.onCardNotLearnedCalled)
    }

    @Test
    fun flashcardToLearnStateWriteMode_whenUserProvidesAnswer_properMethodIsCalled() {
        viewModel.uiState.value = FLASHCARD_TO_LEARN.copy(learnMode = PracticeMode.Write)
        //arrange
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
    fun flashcardToLearnStateWriteMode_whenClicksOnCardNotLearned_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_LEARN.copy(learnMode = PracticeMode.Write)

        //act
        composeTestRule.onNodeWithTag("cardNotLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardNotLearnedCalled)
    }

    @Test
    fun whenLearnModeClickAndUserClicksSettings_properSettingsAreDisplayed() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_LEARN.copy(learnMode = PracticeMode.Click)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.waitForIdle()

        //assert
        composeTestRule.onNodeWithTag(learnModeClickName).assertIsDisplayed()
        composeTestRule.onNodeWithTag(learnModeClickName).assertIsSelected()
        composeTestRule.onNodeWithTag(learnModeWriteName).assertIsDisplayed()
        composeTestRule.onNodeWithTag(learnModeWriteName).assertIsNotSelected()
        composeTestRule.onNodeWithText(caseSensitiveText).assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch").assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch").assertIsNotEnabled()
    }

    @Test
    fun whenLearnModeClickAndUserClicksChangesMode_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_LEARN.copy(learnMode = PracticeMode.Click)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.onNodeWithTag(learnModeWriteName).performClick()

        //assert
        assertEquals(listOf(PracticeMode.Write), viewModel.onPracticeModeChangedCalls)
    }

    @Test
    fun whenLearnModeWriteAndUserClicksChangesMode_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_LEARN.copy(learnMode = PracticeMode.Write)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.onNodeWithTag(learnModeClickName).performClick()

        //assert
        assertEquals(listOf(PracticeMode.Click), viewModel.onPracticeModeChangedCalls)
    }

    @Test
    fun whenTestModeClickAndCaseInsensitiveSettings_properSettingsAreDisplayed() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_LEARN.copy(learnMode = PracticeMode.Write, caseSensitive = false)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()

        //assert
        composeTestRule.onNodeWithTag(learnModeClickName).assertIsDisplayed()
        composeTestRule.onNodeWithTag(learnModeClickName).assertIsNotSelected()
        composeTestRule.onNodeWithTag(learnModeWriteName).assertIsDisplayed()
        composeTestRule.onNodeWithTag(learnModeWriteName).assertIsSelected()
        composeTestRule.onNodeWithText(caseSensitiveText).assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch").assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch").assertIsEnabled()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch").assertIsOff()
    }

    @Test
    fun whenTestModeClickAndUserWriteCaseSensitiveSettings_properSettingsAreDisplayed() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_LEARN.copy(learnMode = PracticeMode.Write, caseSensitive = true)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.waitForIdle()

        //assert
        composeTestRule.onNodeWithTag(learnModeClickName).assertIsDisplayed()
        composeTestRule.onNodeWithTag(learnModeClickName).assertIsNotSelected()
        composeTestRule.onNodeWithTag(learnModeWriteName).assertIsDisplayed()
        composeTestRule.onNodeWithTag(learnModeWriteName).assertIsSelected()
        composeTestRule.onNodeWithText(caseSensitiveText).assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch").assertIsDisplayed()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch").assertIsEnabled()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch").assertIsOn()
    }
    
    @Test
    fun whenTestModeClickAndUserWriteCaseSensitiveSettingsAndUserChangesCaseSensitive_properMethodIsCalled() {
        //arrange
        viewModel.uiState.value = FLASHCARD_TO_LEARN.copy(learnMode = PracticeMode.Write, caseSensitive = true)

        //act
        composeTestRule.onNodeWithContentDescription(settingsButtonContentDescription).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("caseSensitiveSwitch").performClick()

        //assert
        assertEquals(listOf(false), viewModel.onCaseSensitiveChangedCalls)
    }

    @Test
    fun whenViewModelEmitsWrongAnswerState_screenShowsWrongAnswerContent() {
        //arrange
        val wrongAnswer = WrongAnswer(
            setName = "setname",
            cardSetSize = 10,
            learnedCards = 5,
            flashcardToLearn = Flashcard(obverse = "question", reverse = "correct answer"),
            learnMode = PracticeMode.Write,
            caseSensitive = true,
            providedAnswer = "wrong answer"
        )

        //act
        viewModel.uiState.value = wrongAnswer

        //assert
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.learn_card_set_screen_proper_answer)).assertIsDisplayed()
        composeTestRule.onNodeWithText("correct answer").assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.learn_card_set_screen_your_answer)).assertIsDisplayed()
        composeTestRule.onNodeWithText("wrong answer").assertIsDisplayed()
        composeTestRule.onNodeWithTag("cardLearnedButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("cardNotLearnedButton").assertIsDisplayed()
    }

    @Test
    fun whenFlashcardToLearnStateWithShowSuccessToast_onToastShownIsCalled() {
        //arrange
        val state = FLASHCARD_TO_LEARN.copy(
            learnMode = PracticeMode.Write,
            showSuccessToast = true
        )

        //act
        viewModel.uiState.value = state
        composeTestRule.waitForIdle()

        //assert
        assertTrue(viewModel.onToastShownCalled)
    }

    companion object {
        private val FLASHCARD_TO_LEARN = FlashcardToLearn(
            setName = "setname",
            cardSetSize = 17,
            learnedCards = 6,
            flashcardToLearn = Flashcard(
                obverse = "question",
                reverse = "answer"
            ),
            learnMode = PracticeMode.Click,
            caseSensitive = true,
            showSuccessToast = false
        )
    }

}
