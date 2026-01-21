package com.szpejsoft.flashcards.ui.screens.cardsets.learn.learn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.learn.LearnCardSetUiState.FlashcardToLearn
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.learn.LearnCardSetUiState.LearningFinished

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
        Spacer(modifier = Modifier.weight(0.19f))
        FlashCard(state.flashcardToLearn.obverse, state.flashcardToLearn.reverse, Modifier.weight(0.62f))
        Spacer(modifier = Modifier.weight(0.19f))
        Buttons(onCardNotLearned, onCardLearned)
    }
}

@Composable
private fun ColumnScope.FlashCard(
    obverse: String,
    reverse: String,
    modifier: Modifier = Modifier
) {
    var showObverse by remember("$obverse $reverse") { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .clickable(onClick = { showObverse = !showObverse })
            .then(modifier),
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
                text = if (showObverse) obverse else reverse,
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

@Preview
@Composable
fun LearningFinished(
    onBackButtonClicked: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f),
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
