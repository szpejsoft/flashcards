package com.szpejsoft.flashcards.ui.screens.cardsets.test.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.ui.screens.cardsets.test.test.TestCardSetUiState.FlashcardToTest
import com.szpejsoft.flashcards.ui.screens.cardsets.test.test.TestCardSetUiState.TestFinished

@Composable
fun TestCardSetScreen(
    viewModel: TestCardSetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is FlashcardToTest -> TestCardSetContent(
            state as FlashcardToTest,
            viewModel::onCardLearned,
            viewModel::onCardNotLearned
        )

        is TestFinished -> LearningFinished(
            state as TestFinished,
            onNavigateBack
        )
    }
}

@Composable
fun TestCardSetContent(
    state: FlashcardToTest,
    onCardLearned: () -> Unit,
    onCardNotLearned: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            text = state.setName,
        )
        TestProgress(state.learnedCards, state.failedCards, state.cardSetSize)
        Spacer(modifier = Modifier.weight(0.19f))
        FlashCard(state.flashcardToTest.obverse, Modifier.weight(0.62f))
        Spacer(modifier = Modifier.weight(0.19f))
        Buttons(onCardNotLearned, onCardLearned)
    }
}

@Composable
private fun FlashCard(
    obverse: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .then(modifier),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = obverse,
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}

@Composable
private fun Buttons(
    onCardNotLearned: () -> Unit,
    onCardLearned: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
private fun LearningFinished(
    state: TestFinished,
    onBackButtonClicked: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f),
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
