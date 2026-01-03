package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetUiState.Busy
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetUiState.Error
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetUiState.Idle

@Composable
fun EditCardSetScreen(
    viewModel: EditCardSetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is Error -> RenderError(uiState as Error)
        is Idle -> RenderIdle(uiState as Idle, viewModel::onAddFlashcard)
        Busy -> RenderLoading()
    }

}

@Composable
fun RenderLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun RenderIdle(
    state: Idle,
    onAddFlashcard: (String, String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddFlashcardDialog(
            onDismiss = { showDialog = false },
            onConfirm = { obverse, reverse ->
                showDialog = false
                onAddFlashcard(obverse, reverse)
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = state.cardSetName)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = state.flashCards.size,
                    key = { index -> state.flashCards[index].id },
                    itemContent = { index -> FlashcardCard(state.flashCards[index]) }
                )
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.wcag_action_add))
        }
    }
}

@Composable
fun RenderError(error: Error) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = error.message ?: "Unknown error")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFlashcardDialog(
    onDismiss: () -> Unit,
    onConfirm: (obverse: String, reverse: String) -> Unit
) {
    var obverse by remember { mutableStateOf("") }
    var reverse by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.edit_card_set_screen_add_flashcard_dialog_title)) },
        text = {
            Column {
                TextField(
                    value = obverse,
                    onValueChange = { obverse = it },
                    label = { Text(stringResource(R.string.edit_card_set_screen_add_flashcard_dialog_obverse_label)) }
                )
                TextField(
                    value = reverse,
                    onValueChange = { reverse = it },
                    label = { Text(stringResource(R.string.edit_card_set_screen_add_flashcard_dialog_reverse_label)) }
                )
            }
        },
        confirmButton = {
            Button(
                enabled = obverse.isNotBlank() && reverse.isNotBlank(),
                onClick = { onConfirm(obverse, reverse) }
            ) {
                Text(text = stringResource(R.string.action_add))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel))
            }
        }
    )
}