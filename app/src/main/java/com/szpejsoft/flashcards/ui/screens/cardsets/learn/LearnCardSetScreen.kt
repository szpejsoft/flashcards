package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.domain.model.Flashcard
import com.szpejsoft.flashcards.domain.model.PracticeMode
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.FlashcardToLearn
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.LearningFinished
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.WrongAnswer
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModelImpl
import com.szpejsoft.flashcards.ui.screens.common.AnswerProvider
import com.szpejsoft.flashcards.ui.screens.common.PracticeModeSettings

@Composable
fun LearnCardSetScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LearnCardSetViewModel = hiltViewModel<LearnCardSetViewModelImpl>()
) {
    val state by viewModel.uiState.collectAsState()

    when (val s = state) {
        is FlashcardToLearn -> LearnCardSetContent(
            state = s,
            modifier = modifier,
            onCardLearned = viewModel::onCardLearned,
            onCardNotLearned = viewModel::onCardNotLearned,
            onAnswerProvided = viewModel::onAnswerProvided,
            onLearnModeChanged = viewModel::onTestModeChanged,
            onCaseSensitiveChanged = viewModel::onCaseSensitiveChanged,
            onToastShown = viewModel::onToastShown
        )

        LearningFinished -> LearningFinishedContent(
            onBackButtonClicked = onNavigateBack,
            modifier = modifier
        )

        is WrongAnswer -> WrongAnswerContent(
            state = s,
            onCardLearned = viewModel::onCardLearned,
            onCardNotLearned = viewModel::onCardNotLearned,
            onLearnModeChanged = viewModel::onTestModeChanged,
            onCaseSensitiveChanged = viewModel::onCaseSensitiveChanged,
            modifier = modifier
        )
    }
}

@Composable
private fun LearnCardSetContent(
    state: FlashcardToLearn,
    modifier: Modifier = Modifier,
    onCardLearned: () -> Unit,
    onCardNotLearned: () -> Unit,
    onAnswerProvided: (String) -> Unit,
    onLearnModeChanged: (PracticeMode) -> Unit,
    onCaseSensitiveChanged: (Boolean) -> Unit,
    onToastShown: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(state.flashcardToLearn, state.showSuccessToast) {
        if (state.showSuccessToast && state.learnMode == PracticeMode.Write) {
            Toast.makeText(context, R.string.learn_card_set_screen_success_toast, Toast.LENGTH_SHORT).show()
            onToastShown()
        }
    }

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
                currentMode = state.learnMode,
                caseSensitive = state.caseSensitive,
                onTestModeChanged = onLearnModeChanged,
                onCaseSensitiveChanged = onCaseSensitiveChanged,
            )
        }
        LearningProgress(
            learned = state.learnedCards,
            setSize = state.cardSetSize,
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        )
        Spacer(modifier = Modifier.weight(0.19f))
        FlippableFlashCard(
            obverse = state.flashcardToLearn.obverse,
            reverse = state.flashcardToLearn.reverse,
            isFlippable = state.learnMode == PracticeMode.Click,
            modifier = Modifier.weight(0.62f)
        )
        Spacer(modifier = Modifier.weight(0.19f))
        if (state.learnMode == PracticeMode.Click) {
            Buttons(
                showCardLearnedButton = true,
                onCardNotLearned = onCardNotLearned,
                onCardLearned = onCardLearned,

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
private fun WrongAnswerContent(
    state: WrongAnswer,
    onCardLearned: () -> Unit,
    onCardNotLearned: () -> Unit,
    onLearnModeChanged: (PracticeMode) -> Unit,
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
                currentMode = state.learnMode,
                caseSensitive = state.caseSensitive,
                onTestModeChanged = onLearnModeChanged,
                onCaseSensitiveChanged = onCaseSensitiveChanged
            )
        }
        LearningProgress(
            learned = state.learnedCards,
            setSize = state.cardSetSize,
            modifier = Modifier.padding(
                top = 12.dp, bottom = 12.dp
            )
        )
        Spacer(modifier = Modifier.weight(0.19f))
        WrongAnswerCard(state, modifier = Modifier.weight(0.62f))
        Spacer(modifier = Modifier.weight(0.19f))
        Buttons(
            showCardLearnedButton = state.learnMode == PracticeMode.Write,
            onCardNotLearned = onCardNotLearned,
            onCardLearned = onCardLearned
        )
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
private fun WrongAnswerCard(
    state: WrongAnswer,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
            ) {
                Text(text = stringResource(R.string.learn_card_set_screen_proper_answer))
                Box(
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.flashcardToLearn.reverse,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
            }
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
            ) {
                Text(
                    text = stringResource(R.string.learn_card_set_screen_your_answer),
                    color = MaterialTheme.colorScheme.error
                )
                Box(
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.providedAnswer,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Composable
private fun Buttons(
    showCardLearnedButton: Boolean,
    onCardNotLearned: () -> Unit,
    onCardLearned: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
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
        if (showCardLearnedButton) {
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
}

@Composable
private fun LearningFinishedContent(
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
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.learn_card_set_screen_learning_finished_title),
                style = MaterialTheme.typography.headlineLarge
            )
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
private fun LearnCardSetContentPreview() {
    LearnCardSetContent(
        state = PreviewFlashcard,
        onCardLearned = {},
        onCardNotLearned = {},
        onAnswerProvided = {},
        onLearnModeChanged = {},
        onCaseSensitiveChanged = {},
        onToastShown = {}
    )
}

@Preview(showBackground = true, name = "Wrong Answer")
@Composable
private fun WrongAnswerContentPreview() {
    WrongAnswerContent(
        state = WrongAnswer(
            setName = "Preview Set",
            cardSetSize = 10,
            learnedCards = 5,
            flashcardToLearn = Flashcard(1, "Question", "Correct Answer"),
            learnMode = PracticeMode.Write,
            caseSensitive = false,
            providedAnswer = "Wrong Answer"
        ),
        onCardLearned = {},
        onCardNotLearned = {},
        onLearnModeChanged = {},
        onCaseSensitiveChanged = {}
    )
}

@Preview(showBackground = true, name = "Finished")
@Composable
private fun LearningFinishedPreview() {
    LearningFinishedContent(onBackButtonClicked = {})
}

private val PreviewFlashcard = FlashcardToLearn(
    setName = "set",
    cardSetSize = 7,
    learnedCards = 3,
    flashcardToLearn = Flashcard(1, "question", "answer"),
    learnMode = PracticeMode.Click,
    caseSensitive = false,
    showSuccessToast = false
)
