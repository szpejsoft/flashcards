package com.szpejsoft.flashcards.ui.screens.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.szpejsoft.flashcards.R

@Composable
fun Toolbox(
    enabled: Boolean,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClicked: (() -> Unit)? = null,
    onEditClicked: (() -> Unit)? = null
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        onEditClicked?.let {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.action_edit)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(R.string.action_edit)
                    )
                },
                onClick = onEditClicked,
                enabled = enabled
            )
        }
        onDeleteClicked?.let {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.action_delete)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.action_delete)
                    )
                },
                onClick = onDeleteClicked,
                enabled = enabled
            )
        }
    }
}
