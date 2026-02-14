package com.szpejsoft.flashcards.ui.screens.cardsets.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetListViewModel
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetListViewModelImpl
import com.szpejsoft.flashcards.ui.screens.common.CardSetCard
import com.szpejsoft.flashcards.ui.screens.common.ScreenBackground
import com.szpejsoft.flashcards.ui.screens.common.Toolbox

@Composable
fun EditCardSetListScreen(
    onAddButtonClick: () -> Unit,
    onEditButtonClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditCardSetListViewModel = hiltViewModel<EditCardSetListViewModelImpl>(),
) {
    val uiState by viewModel.uiState.collectAsState()

    EditCardSetListScreenContent(
        uiState = uiState,
        onAddButtonClick = onAddButtonClick,
        onEditButtonClick = onEditButtonClick,
        onDeleteCardSetClicked = viewModel::onDeleteCardSetClicked,
        modifier = modifier
    )
}

@Composable
private fun EditCardSetListScreenContent(
    uiState: EditCardSetListViewModel.UiState,
    onAddButtonClick: () -> Unit,
    onEditButtonClick: (Long) -> Unit,
    onDeleteCardSetClicked: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expandedCardId by rememberSaveable { mutableStateOf<Long?>(null) }
    val cardSets = uiState.cardSets

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        ScreenBackground(Icons.Outlined.Edit)
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
                        onClick = { expandedCardId = cardSetId }
                    )
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Toolbox(
                            enabled = true,
                            expanded = expandedCardId == cardSetId,
                            onDismissRequest = { expandedCardId = null },
                            onDeleteClicked = {
                                onDeleteCardSetClicked(cardSetId)
                                expandedCardId = null
                            },
                            onEditClicked = {
                                onEditButtonClick(cardSetId)
                                expandedCardId = null
                            }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddButtonClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, stringResource(R.string.wcag_action_add))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditCardSetListScreenPreview() {
    EditCardSetListScreenContent(
        uiState = EditCardSetListViewModel.UiState(emptyList()),
        onAddButtonClick = {},
        onEditButtonClick = {},
        onDeleteCardSetClicked = {}
    )
}
