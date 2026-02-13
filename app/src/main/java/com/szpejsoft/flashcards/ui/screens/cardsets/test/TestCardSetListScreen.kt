package com.szpejsoft.flashcards.ui.screens.cardsets.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.domain.model.CardSet
import com.szpejsoft.flashcards.presentation.test.TestCardSetListViewModel
import com.szpejsoft.flashcards.presentation.test.TestCardSetListViewModelImpl
import com.szpejsoft.flashcards.ui.screens.common.CardSetCard

@Composable
fun TestCardSetListScreen(
    onTestButtonClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TestCardSetListViewModel = hiltViewModel<TestCardSetListViewModelImpl>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cardSets = uiState.cardSets

    TestCardScreenContent(
        modifier = modifier,
        cardSets = cardSets,
        onTestButtonClick = onTestButtonClick
    )

}

@Composable
private fun TestCardScreenContent(
    modifier: Modifier,
    cardSets: List<CardSet>,
    onTestButtonClick: (Long) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                count = cardSets.size,
                key = { index -> cardSets[index].id },
            ) { index ->
                Box {
                    val cardSetId = cardSets[index].id
                    CardSetCard(
                        cardSet = cardSets[index],
                        onClick = { onTestButtonClick(cardSetId) }
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestCardScreenContentPreview() {
    TestCardScreenContent(
        modifier = Modifier,
        cardSets = listOf(CardSet(1, "name 1"), CardSet(2, "name 2")),
        onTestButtonClick = {}
    )
}