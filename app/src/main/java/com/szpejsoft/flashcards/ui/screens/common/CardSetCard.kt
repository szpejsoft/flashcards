package com.szpejsoft.flashcards.ui.screens.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.szpejsoft.flashcards.domain.model.CardSet

@Composable
fun CardSetCard(
    cardSet: CardSet,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(cardSet.id) }),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = cardSet.name,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleSmall
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun CardSetCardPreview() {
    CardSetCard(
        cardSet = CardSet(1, "name"),
        onClick = {},
        modifier = Modifier
    )
}