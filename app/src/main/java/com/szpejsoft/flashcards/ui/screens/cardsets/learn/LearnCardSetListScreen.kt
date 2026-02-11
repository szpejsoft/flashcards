package com.szpejsoft.flashcards.ui.screens.cardsets.learn

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
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetListViewModel
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetListViewModelImpl
import com.szpejsoft.flashcards.ui.screens.common.CardSetCard

@Composable
fun LearnCardSetListScreen(
    onLearnButtonClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LearnCardSetListViewModel = hiltViewModel<LearnCardSetListViewModelImpl>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cardSets = uiState.cardSets

    LazyColumn(
        modifier = modifier.fillMaxSize(),
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
                    onClick = { onLearnButtonClick(cardSetId) }
                )

            }
        }
    }

}
