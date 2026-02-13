package com.szpejsoft.flashcards.ui.screens.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.szpejsoft.flashcards.domain.model.Flashcard

@Composable
fun ClickableFlashcard(
    flashCard: Flashcard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = flashCard.obverse,
                style = MaterialTheme.typography.headlineMedium
            )
            HorizontalDivider()
            Text(
                text = flashCard.reverse,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ClickableFlashcardPreview() {
    ClickableFlashcard(
        flashCard = Flashcard(1, "question", "answer"),
        onClick = {},
        modifier = Modifier
    )
}