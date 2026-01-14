package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.LearnCardSetUiState.FlashcardToLearn
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.LearnCardSetUiState.Success

@Composable
fun LearnCardSetScreen(
    viewModel: LearnCardSetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is FlashcardToLearn -> LearnCardSetContent(state as FlashcardToLearn, viewModel::onCardLearned)
        Success -> Success(onNavigateBack)
    }

}

@Composable
fun LearnCardSetContent(
    state: FlashcardToLearn,
    onCardLearned: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),

        ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "${state.learnedCards}/${state.cardSetSize}"
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCardLearned
        ) {
            Text("Learned")
        }
    }
}


@Composable
fun Success(
    onBackButtonClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Success"
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBackButtonClicked
        ) {
            Text("Back")
        }
    }
}