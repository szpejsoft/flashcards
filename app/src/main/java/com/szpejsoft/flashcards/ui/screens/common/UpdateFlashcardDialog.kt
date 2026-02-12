package com.szpejsoft.flashcards.ui.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.szpejsoft.flashcards.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateFlashcardDialog(
    flashcardId: Long,
    obverse: String,
    reverse: String,
    onDismiss: () -> Unit,
    onConfirm: (flashcardId: Long, obverse: String, reverse: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var obverseState by remember { mutableStateOf(obverse) }
    var reverseState by remember { mutableStateOf(reverse) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.edit_card_set_screen_update_flashcard_dialog_title)) },
        text = {
            Column {
                TextField(
                    modifier = Modifier.testTag("update_dialog_obverse_field"),
                    value = obverseState,
                    onValueChange = { obverseState = it },
                    label = { Text(stringResource(R.string.edit_card_set_screen_edit_flashcard_dialog_obverse_label)) }
                )
                TextField(
                    modifier = Modifier.testTag("update_dialog_reverse_field"),
                    value = reverseState,
                    onValueChange = { reverseState = it },
                    label = { Text(stringResource(R.string.edit_card_set_screen_edit_flashcard_dialog_reverse_label)) }
                )
            }
        },
        confirmButton = {
            Button(
                enabled = obverseState.isNotBlank() && reverseState.isNotBlank(),
                onClick = { onConfirm(flashcardId, obverseState, reverseState) }
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

@Preview
@Composable
private fun UpdateFlashcardDialogPreview() {
    UpdateFlashcardDialog(
        flashcardId = 1L,
        obverse = "Apple",
        reverse = "Jabłko",
        onDismiss = {},
        onConfirm = { _, _, _ -> }
    )
}
