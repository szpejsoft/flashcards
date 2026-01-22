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
import androidx.compose.ui.res.stringResource
import com.szpejsoft.flashcards.R


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
