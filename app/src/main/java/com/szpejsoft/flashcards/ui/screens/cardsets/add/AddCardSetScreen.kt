package com.szpejsoft.flashcards.ui.screens.cardsets.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import kotlinx.coroutines.delay

@Composable
fun AddCardSetScreen(
    viewModel: AddCardSetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(uiState) {
        delay(300) //navigate after user sees checkmark
        if (uiState is AddCardSetUiState.Saved) {
            onNavigateBack()
        }
    }

    if (showError && (uiState as? AddCardSetUiState.Editing)?.isSaveEnabled == true) {
        showError = false
    }

    when (val state = uiState) {
        is AddCardSetUiState.Editing -> {
            AddCardSetContent(
                cardSetName = state.name,
                isSaveEnabled = state.isSaveEnabled,
                isError = showError,
                onNameChange = viewModel::onCardSetNameChanged,
                onSaveClick = {
                    if (state.isSaveEnabled) {
                        viewModel.onSaveClicked()
                    } else {
                        showError = true
                    }
                }
            )
        }

        is AddCardSetUiState.Saving -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is AddCardSetUiState.Error -> {
            Column {
                AddCardSetContent(
                    cardSetName = "", // Or restore previous name
                    isSaveEnabled = false,
                    isError = showError,
                    onNameChange = {},
                    onSaveClick = {}
                )
                Text(text = state.message?:stringResource(R.string.database_save_error_message), color = MaterialTheme.colorScheme.error)
            }
        }

        is AddCardSetUiState.Saved -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.CheckCircleOutline, stringResource(R.string.wcag_description_success))
            }
        }
    }
}

@Composable
fun AddCardSetContent(
    cardSetName: String,
    isSaveEnabled: Boolean,
    isError: Boolean,
    onNameChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.add_card_set_screen_card_set_title_hint)) },
            value = cardSetName,
            onValueChange = onNameChange,
            singleLine = true,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = stringResource(R.string.add_card_set_screen_card_set_title_empty_error_message),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSaveClick() }
            )
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = isSaveEnabled,
            onClick = onSaveClick
        ) {
            Text(stringResource(R.string.action_save))
        }
    }
}