package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.LearnCardSetUiState.FlashcardToLearn
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.LearnCardSetUiState.LearningFinished

@Composable
fun LearnCardSetScreen(
    viewModel: LearnCardSetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is FlashcardToLearn -> LearnCardSetContent(
            state as FlashcardToLearn,
            viewModel::onCardLearned,
            viewModel::onCardNotLearned
        )

        LearningFinished -> LearningFinished(onNavigateBack)
    }

}

@Composable
fun LearnCardSetContent(
    state: FlashcardToLearn,
    onCardLearned: () -> Unit,
    onCardNotLearned: () -> Unit
) {
    var showObverse by remember(state) { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            text = state.setName,
        )
        LearningProgress(state.learnedCards, state.cardSetSize)

        Card(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { showObverse = !showObverse }),
            shape = RoundedCornerShape(16.dp)
        )
        {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = if (showObverse) state.flashcardToLearn.obverse else state.flashcardToLearn.reverse,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
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
fun LearningFinished(
    onBackButtonClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
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