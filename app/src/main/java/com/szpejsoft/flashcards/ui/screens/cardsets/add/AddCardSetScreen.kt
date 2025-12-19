package com.szpejsoft.flashcards.ui.screens.cardsets.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun AddCardSetScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddCardSetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        delay(300) //navigate after user sees checkmark
        if (uiState is AddCardSetUiState.Saved) {
            onNavigateBack()
        }
    }

    when (val state = uiState) {
        is AddCardSetUiState.Editing -> {
            AddCardSetContent(
                cardSetName = state.name,
                isSaveEnabled = state.isSaveEnabled,
                onNameChange = viewModel::onCardSetNameChanged,
                onSaveClick = viewModel::onSaveClicked
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
                    onNameChange = viewModel::onCardSetNameChanged,
                    onSaveClick = viewModel::onSaveClicked
                )
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is AddCardSetUiState.Saved -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.CheckCircleOutline, "success")
            }
        }
    }
}

@Composable
fun AddCardSetContent(
    cardSetName: String,
    isSaveEnabled: Boolean,
    onNameChange: (String) -> Unit,
    onSaveClick: () -> Unit

) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.weight(0.5f))

        TextField(
            value = cardSetName,
            onValueChange = onNameChange,
            label = { Text("Enter your text") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(0.5f))

        Button(
            enabled = isSaveEnabled,
            onClick = onSaveClick
        ) {
            Text("Save")
        }
    }
}