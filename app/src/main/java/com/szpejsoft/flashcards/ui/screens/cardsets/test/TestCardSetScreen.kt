package com.szpejsoft.flashcards.ui.screens.cardsets.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.model.PracticeMode
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState.FlashcardToTest
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState.TestFinished
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModelImpl
import com.szpejsoft.flashcards.ui.screens.common.AnswerProvider
import com.szpejsoft.flashcards.ui.screens.common.PracticeModeSettings

@Composable
fun TestCardSetScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TestCardSetViewModel = hiltViewModel<TestCardSetViewModelImpl>()
) {
    val state by viewModel.uiState.collectAsState()

    when (val s = state) {
        is FlashcardToTest -> TestCardSetContent(
            state = s,
            onCardLearned = viewModel::onCardLearned,
            onCardNotLearned = viewModel::onCardNotLearned,
            onAnswerProvided = viewModel::onAnswerProvided,
            onTestModeChanged = viewModel::onTestModeChanged,
            onCaseSensitiveChanged = viewModel::onCaseSensitiveChanged,
            modifier = modifier
        )

        is TestFinished -> TestingFinishedContent(
            state = s,
            onBackButtonClicked = onNavigateBack,
            modifier = modifier
        )
    }
}

@Composable
private fun TestCardSetContent(
    state: FlashcardToTest,
    onCardLearned: () -> Unit,
    onCardNotLearned: () -> Unit,
    onAnswerProvided: (String) -> Unit,
    onTestModeChanged: (PracticeMode) -> Unit,
    onCaseSensitiveChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Row {
            Text(
                modifier = Modifier.weight(1.0f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                text = state.setName,
            )
            PracticeModeSettings(
                currentMode = state.testMode,
                caseSensitive = state.caseSensitive,
                onTestModeChanged = onTestModeChanged,
                onCaseSensitiveChanged = onCaseSensitiveChanged
            )
        }
        TestProgress(
            learned = state.learnedCards,
            failed = state.failedCards,
            setSize = state.cardSetSize,
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 12.dp
            )
        )
        Spacer(modifier = Modifier.weight(0.19f))
        OneSideFlashcard(state.flashcardToTest.obverse, Modifier.weight(0.62f))
        Spacer(modifier = Modifier.weight(0.19f))
        if (state.testMode == PracticeMode.Click) {
            Buttons(
                onCardNotLearned = onCardNotLearned,
                onCardLearned = onCardLearned,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        } else {
            AnswerProvider(
                onSkipAnswer = onCardNotLearned,
                onAnswerProvided = onAnswerProvided,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
private fun Buttons(
    onCardNotLearned: () -> Unit,
    onCardLearned: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            modifier = Modifier
                .weight(0.5f)
                .testTag("cardNotLearnedButton"),
            onClick = onCardNotLearned
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.wcag_button_not_learned_description)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            modifier = Modifier
                .weight(0.5f)
                .testTag("cardLearnedButton"),
            onClick = onCardLearned
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.wcag_button_learned_description)
            )
        }
    }
}

@Composable
private fun TestingFinishedContent(
    state: TestFinished,
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1.0f),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.test_card_set_screen_test_finished_title),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(
                        R.string.test_card_set_screen_test_finished_score,
                        state.learnedCards,
                        state.cardSetSize
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBackButtonClicked
        ) {
            Text(stringResource(R.string.learn_card_set_screen_learning_go_to_card_set_list))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestCardSetContentPreview() {
    TestCardSetContent(
        state = FlashcardToTest(
            setName = "Preview Set",
            cardSetSize = 10,
            learnedCards = 5,
            failedCards = 2,
            flashcardToTest = Flashcard(1, "Question", "Answer"),
            testMode = PracticeMode.Click,
            caseSensitive = false
        ),
        onCardLearned = {},
        onCardNotLearned = {},
        onAnswerProvided = {},
        onTestModeChanged = {},
        onCaseSensitiveChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun TestingFinishedContentPreview() {
    TestingFinishedContent(
        state = TestFinished(
            learnedCards = 8,
            cardSetSize = 10
        ),
        onBackButtonClicked = {}
    )
}
