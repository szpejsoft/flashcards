package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetUiState.Error
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetUiState.Idle
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetUiState.Loading

@Composable
fun EditCardSetScreen(
    viewModel: EditCardSetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is Error -> RenderError(uiState as Error)
        is Idle -> RenderIdle(uiState as Idle)
        Loading -> RenderLoading()
    }


}

@Composable
fun RenderLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun RenderIdle(state: Idle) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = state.cardSetName)

        }

        FloatingActionButton(
            onClick = {},
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