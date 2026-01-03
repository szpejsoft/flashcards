package com.szpejsoft.flashcards.ui.screens.cardsets.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R

@Composable
fun CardSetListScreen(
    viewModel: CardSetListViewModel = hiltViewModel(),
    onAddButtonClick: () -> Unit,
    onEditButtonClick: (Long) -> Unit
) {
    val uiState by viewModel.cardSets.collectAsState()
    var expandedCardId by rememberSaveable { mutableStateOf<Long?>(null) }
    val cardSets = (uiState as? CardSetListUiState.Idle)?.cardSets ?: emptyList()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
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
                        CardSetToolbox(
                            expanded = expandedCardId == cardSetId,
                            onDeleteClicked = {
                                viewModel.onDeleteCardSetClicked(cardSetId)
                                expandedCardId = null
                            },
                            onEditClicked = {
                                onEditButtonClick(cardSetId)
                                expandedCardId = null
                            },
                            onDismissRequest = { expandedCardId = null }
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

@Composable
fun CardSetToolbox(
    expanded: Boolean,
    onDeleteClicked: () -> Unit = {},
    onEditClicked: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.card_set_list_screen_action_edit)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = stringResource(R.string.card_set_list_screen_action_edit)
                )
            },
            onClick = onEditClicked
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.card_set_list_screen_action_delete)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.card_set_list_screen_action_delete)
                )
            },
            onClick = onDeleteClicked
        )
    }
}