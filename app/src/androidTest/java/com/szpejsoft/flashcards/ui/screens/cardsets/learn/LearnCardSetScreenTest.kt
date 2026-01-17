package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.domain.model.Flashcard
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
        viewModel.uiState.value = LearnCardSetUiState.LearningFinished

        //assert
        composeTestRule.onNodeWithText(successText).assertIsDisplayed()
        composeTestRule.onNodeWithText(goToListButton).assertIsDisplayed()
    }

    @Test
    fun whenInFinishedStateAndGoToListClicked_navigateBackIsCalled() {
        //arrange
        viewModel.uiState.value = LearnCardSetUiState.LearningFinished

        //act
        composeTestRule.onNodeWithText(goToListButton).performClick()

        //assert
        assertEquals(1, navigateBackCalls)
    }


    @Test
    fun whenViewModelEmitsFlashcardToLearnState_screenShowsFlashcardToLearn() {
        //arrange

        //act
        viewModel.uiState.value = TEST_FLASHCARD_TO_LEARN

        //assert
        composeTestRule.onNodeWithText("setname").assertIsDisplayed()
        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithText("answer").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("learningProgress").assertTextEquals("6/17")
    }

    @Test
    fun flashcardToLearnState_whenUserClicksQuestion_answerIsDisplayed() {
        //arrange

        //act
        viewModel.uiState.value = TEST_FLASHCARD_TO_LEARN
        composeTestRule.onNodeWithText("question").performClick()

        //assert
        composeTestRule.onNodeWithText("question").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("answer").assertIsDisplayed()
    }

    @Test
    fun flashcardToLearnState_whenUserClicksOnCardNotLearned_properMethodIsCalled() {
        //arrange

        //act
        viewModel.uiState.value = TEST_FLASHCARD_TO_LEARN
        composeTestRule.onNodeWithTag("cardNotLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardNotLearnedCalled)
        assertFalse(viewModel.onCardLearnedCalled)
    }

    @Test
    fun flashcardToLearnState_whenUserClicksOnCardLearned_properMethodIsCalled() {
        //arrange

        //act
        viewModel.uiState.value = TEST_FLASHCARD_TO_LEARN
        composeTestRule.onNodeWithTag("cardLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardLearnedCalled)
        assertFalse(viewModel.onCardNotLearnedCalled)
    }

    companion object {
        private val TEST_FLASHCARD_TO_LEARN = LearnCardSetUiState.FlashcardToLearn(
            setName = "setname",
            cardSetSize = 17,
            learnedCards = 6,
            flashcardToLearn = Flashcard(
                obverse = "question",
                reverse = "answer"
            )
        )
    }

}
