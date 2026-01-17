package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.ui.screens.common.Toolbox

@Composable
fun EditCardSetScreen(
    viewModel: EditCardSetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var showAddFlashCardDialog by remember { mutableStateOf(false) }
    var expandedCardId by remember { mutableStateOf<Long?>(null) }
    var editedFlashcardId by remember { mutableStateOf<Long?>(null) }
    var isActionInProgress by remember { mutableStateOf(false) }
    var setName by remember(state.setName) { mutableStateOf(state.setName) }

    LaunchedEffect(state.flashCards) {
        isActionInProgress = false
    }

    if (showAddFlashCardDialog) {
        AddFlashcardDialog(
            onDismiss = { showAddFlashCardDialog = false },
            onConfirm = { obverse, reverse ->
                showAddFlashCardDialog = false
                viewModel.onAddFlashcard(obverse, reverse)
            }
        )
    }

    if (editedFlashcardId != null) {
        val id = editedFlashcardId ?: return
        val flashcard = state.flashCards.first { it.id == id }

        UpdateFlashcardDialog(
            flashcardId = id,
            obverse = flashcard.obverse,
            reverse = flashcard.reverse,
            onConfirm = { id, obverse, reverse ->
                viewModel.onUpdateFlashcard(id, obverse, reverse)
                editedFlashcardId = null
            },
            onDismiss = { editedFlashcardId = null },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                value = setName,
                onValueChange = { setName = it },
                label = { Text(stringResource(R.string.add_card_set_screen_card_set_title_hint)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.onUpdateCardSetName(setName)
                        //todo clear focus and hide keyboard
                    }
                )
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    count = state.flashCards.size,
                    // key = { index -> state.flashCards[index].id }  TODO
                ) { index ->
                    Box {
                        val flashcardId = state.flashCards[index].id
                        FlashcardCard(
                            flashCard = state.flashCards[index],
                            onClick = {
                                expandedCardId = flashcardId
                                editedFlashcardId = null
                                showAddFlashCardDialog = false
                            }
                        )
                        Box(modifier = Modifier.align(Alignment.TopEnd)) {
                            Toolbox(
                                enabled = !isActionInProgress,
                                expanded = expandedCardId == flashcardId,
                                onDismissRequest = { expandedCardId = null },
                                onDeleteClicked = {
                                    isActionInProgress = true
                                    viewModel.onDeleteFlashcard(flashcardId)
                                },
                                onEditClicked = {
                                    editedFlashcardId = flashcardId
                                    expandedCardId = null
                                    showAddFlashCardDialog = false
                                }
                            )
                        }
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isModified,
                onClick = {
                    viewModel.onSaveClicked()
                    onNavigateBack()
                }
            ) {
                Text(stringResource(R.string.action_save))
            }
        }

        FloatingActionButton(
            onClick = { if (!isActionInProgress) showAddFlashCardDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.wcag_action_add))
        }
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
                    label = { Text(stringResource(R.string.edit_card_set_screen_edit_flashcard_dialog_obverse_label)) }
                )
                TextField(
                    value = reverse,
                    onValueChange = { reverse = it },
                    label = { Text(stringResource(R.string.edit_card_set_screen_edit_flashcard_dialog_reverse_label)) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateFlashcardDialog(
    flashcardId: Long,
    obverse: String,
    reverse: String,
    onDismiss: () -> Unit,
    onConfirm: (flashcardId: Long, obverse: String, reverse: String) -> Unit
) {
    var obverse by remember { mutableStateOf(obverse) }
    var reverse by remember { mutableStateOf(reverse) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.edit_card_set_screen_update_flashcard_dialog_title)) },
        text = {
            Column {
                TextField(
                    modifier = Modifier.testTag("update_dialog_obverse_field"),
                    value = obverse,
                    onValueChange = { obverse = it },
                    label = { Text(stringResource(R.string.edit_card_set_screen_edit_flashcard_dialog_obverse_label)) }
                )
                TextField(
                    modifier = Modifier.testTag("update_dialog_reverse_field"),
                    value = reverse,
                    onValueChange = { reverse = it },
                    label = { Text(stringResource(R.string.edit_card_set_screen_edit_flashcard_dialog_reverse_label)) }
                )
            }
        },
        confirmButton = {
            Button(
                enabled = obverse.isNotBlank() && reverse.isNotBlank(),
                onClick = { onConfirm(flashcardId, obverse, reverse) }
            ) {
                Text(text = stringResource(R.string.action_update))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel))
            }
        }
    )
}
