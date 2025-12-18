package com.szpejsoft.flashcards.ui.screens.cardsets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AddCardSetScreen(
    onSaveButtonClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            textAlign = TextAlign.Center,
            text = "AddCardSetScreen"
        )

        Button(
            onClick = onSaveButtonClick
        ) {
            Text("Save")
        }
    }
}