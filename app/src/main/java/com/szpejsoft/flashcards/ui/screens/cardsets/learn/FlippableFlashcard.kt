package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
internal fun FlippableFlashCard(
    obverse: String,
    reverse: String,
    isFlippable: Boolean,
    modifier: Modifier = Modifier
) {
    var showObverse by remember("$obverse $reverse") { mutableStateOf(true) }
    Card(
        modifier = modifier,
        onClick = {
            if (isFlippable) {
                showObverse = !showObverse
            }
        },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = if (showObverse) obverse else reverse,
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlippableFlashCardPreview() {
    FlippableFlashCard(
        obverse = "question",
        reverse = "answer",
        isFlippable = true,
        modifier = Modifier
    )
}