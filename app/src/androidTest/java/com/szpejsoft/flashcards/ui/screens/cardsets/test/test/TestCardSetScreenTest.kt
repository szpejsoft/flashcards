package com.szpejsoft.flashcards.ui.screens.cardsets.test.test

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
import com.szpejsoft.flashcards.presentation.test.TestCardSetUiState.FlashcardToTest
import com.szpejsoft.flashcards.presentation.test.TestCardSetUiState.TestFinished
import com.szpejsoft.flashcards.presentation.test.TestMode
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

    private var navigateBackCalls = 0

    private lateinit var viewModel: TestCardSetViewModelTestDouble


    @Before
    fun setUp() {
        viewModel = TestCardSetViewModelTestDouble()
        composeTestRule.setContent {
            TestCardSetScreen(viewModel = viewModel, onNavigateBack = { navigateBackCalls++ })
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
        viewModel.uiState.value = TEST_FLASHCARD_TO_TEST

        //assert
        composeTestRule.onNodeWithText("setname").assertIsDisplayed()
        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithText("answer").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("learningProgress").assertTextEquals("6/17")
    }

    @Test
    fun flashcardToTestState_whenUserClicksOnCardNotLearned_properMethodIsCalled() {
        //arrange

        //act
        viewModel.uiState.value = TEST_FLASHCARD_TO_TEST
        composeTestRule.onNodeWithTag("cardNotLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardNotLearnedCalled)
        assertFalse(viewModel.onCardLearnedCalled)
    }

    @Test
    fun flashcardToLearnState_whenUserClicksOnCardLearned_properMethodIsCalled() {
        //arrange

        //act
        viewModel.uiState.value = TEST_FLASHCARD_TO_TEST
        composeTestRule.onNodeWithTag("cardLearnedButton").performClick()

        //assert
        assertTrue(viewModel.onCardLearnedCalled)
        assertFalse(viewModel.onCardNotLearnedCalled)
    }

    companion object {
        private val TEST_FLASHCARD_TO_TEST = FlashcardToTest(
            setName = "setname",
            cardSetSize = 17,
            learnedCards = 6,
            failedCards = 3,
            flashcardToTest = Flashcard(
                obverse = "question",
                reverse = "answer"
            ),
            testMode = TestMode.Click,
            caseSensitive = true
        )
    }

}
