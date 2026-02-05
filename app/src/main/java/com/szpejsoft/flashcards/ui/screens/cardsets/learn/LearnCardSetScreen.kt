package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.szpejsoft.flashcards.domain.model.PracticeMode
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.FlashcardToLearn
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel.UiState.LearningFinished
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModelImpl
import com.szpejsoft.flashcards.ui.screens.common.AnswerProvider
import com.szpejsoft.flashcards.ui.screens.common.PracticeModeSettings

@Composable
fun LearnCardSetScreen(
    viewModel: LearnCardSetViewModel = hiltViewModel<LearnCardSetViewModelImpl>(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is FlashcardToLearn -> LearnCardSetContent(
            state as FlashcardToLearn,
            viewModel::onCardLearned,
            viewModel::onCardNotLearned,
            viewModel::onAnswerProvided,
            viewModel::onTestModeChanged,
            viewModel::onCaseSensitiveChanged
        )

        LearningFinished -> LearningFinished(onNavigateBack)
    }

}

@Composable
fun LearnCardSetContent(
    state: FlashcardToLearn,
    onCardLearned: () -> Unit,
    onCardNotLearned: () -> Unit,
    onAnswerProvided: (String) -> Unit,
    onLearnModeChanged: (PracticeMode) -> Unit,
    onCaseSensitiveChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
        LearningProgress(state.learnedCards, state.cardSetSize)
        Spacer(modifier = Modifier.weight(0.19f))
        FlippableFlashCard(
            obverse = state.flashcardToLearn.obverse,
            reverse = state.flashcardToLearn.reverse,
            isFlippable = state.learnMode == PracticeMode.Click,
            modifier = Modifier.weight(0.62f)
        )
        Spacer(modifier = Modifier.weight(0.19f))
        if (state.learnMode == PracticeMode.Click) {
            Buttons(onCardNotLearned, onCardLearned)
        } else {
            AnswerProvider(onCardNotLearned, onAnswerProvided)
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
private fun Buttons(
    onCardNotLearned: () -> Unit,
    onCardLearned: () -> Unit
) {
    Row(
        modifier = Modifier
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

@Preview
@Composable
fun LearningFinished(
    onBackButtonClicked: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
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
