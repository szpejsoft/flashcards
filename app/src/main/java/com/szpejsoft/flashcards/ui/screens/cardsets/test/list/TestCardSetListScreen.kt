package com.szpejsoft.flashcards.ui.screens.cardsets.test.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.presentation.test.TestCardSetListViewModel
import com.szpejsoft.flashcards.presentation.test.TestCardSetListViewModelImpl
import com.szpejsoft.flashcards.ui.screens.common.CardSetCard

@Composable
fun TestCardSetListScreen(
    viewModel: TestCardSetListViewModel = hiltViewModel<TestCardSetListViewModelImpl>(),
    onTestButtonClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val cardSets = uiState.cardSets

    Box(modifier = Modifier.fillMaxSize()) {
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
