package com.szpejsoft.flashcards.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.domain.model.PracticeMode


@Composable
fun PracticeModeSettings(
    currentMode: PracticeMode,
    caseSensitive: Boolean,
    onTestModeChanged: (PracticeMode) -> Unit,
    onCaseSensitiveChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSettingsMenuVisible by remember { mutableStateOf(false) }

    PracticeModeSettingsContent(
        isSettingsMenuVisible = isSettingsMenuVisible,
        currentMode = currentMode,
        caseSensitive = caseSensitive,
        onCaseSensitiveChanged = onCaseSensitiveChanged,
        onDismissMenu = { isSettingsMenuVisible = false },
        onTestModeChanged = onTestModeChanged,
        onToggleMenu = { isSettingsMenuVisible = !isSettingsMenuVisible },
        modifier = modifier
    )
}

@Composable
private fun PracticeModeSettingsContent(
    isSettingsMenuVisible: Boolean,
    currentMode: PracticeMode,
    caseSensitive: Boolean,
    onCaseSensitiveChanged: (Boolean) -> Unit,
    onDismissMenu: () -> Unit,
    onTestModeChanged: (PracticeMode) -> Unit,
    onToggleMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        IconButton(onClick = onToggleMenu) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.wcag_button_settings_description)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .selectableGroup()
        ) {
            DropdownMenu(
                expanded = isSettingsMenuVisible,
                onDismissRequest = onDismissMenu
            ) {
                PracticeModeMenuItem(
                    practiceMode = PracticeMode.Click,
                    isSelected = PracticeMode.Click == currentMode,
                    onClick = { onTestModeChanged(PracticeMode.Click) },
                    modifier = Modifier.height(56.dp)
                )
                HorizontalDivider()
                PracticeModeMenuItem(
                    practiceMode = PracticeMode.Write,
                    isSelected = PracticeMode.Write == currentMode,
                    onClick = { onTestModeChanged(PracticeMode.Write) },
                    modifier = Modifier.height(56.dp)
                )
                DropdownMenuItem(
                    modifier = Modifier
                        .height(56.dp)
                        .align(Alignment.Start)
                        .testTag("caseSensitiveSwitch")
                        .semantics {
                            toggleableState = if (caseSensitive) ToggleableState.On else ToggleableState.Off
                            role = Role.Switch
                        },
                    enabled = currentMode == PracticeMode.Write,
                    onClick = { onCaseSensitiveChanged(!caseSensitive) },
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1.0f)
                                    .padding(end = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                text = stringResource(R.string.practice_settings_menu_case_sensitive),
                                textAlign = TextAlign.Start,
                            )
                            Switch(
                                modifier = Modifier.padding(4.dp),
                                checked = caseSensitive,
                                onCheckedChange = null,
                                enabled = currentMode == PracticeMode.Write
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.PracticeModeMenuItem(
    practiceMode: PracticeMode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenuItem(
        modifier = modifier
            .align(Alignment.Start)
            .testTag(practiceMode.getName())
            .semantics {
                selected = isSelected
                role = Role.RadioButton
            },
        onClick = onClick,
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(end = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    text = practiceMode.getName(),
                    textAlign = TextAlign.Start
                )
                RadioButton(
                    selected = isSelected,
                    onClick = null
                )
            }
        }
    )
}

@Composable
private fun PracticeMode.getName(): String = when (this) {
    PracticeMode.Click -> stringResource(R.string.practice_settings_menu_practice_mode_click)
    PracticeMode.Write -> stringResource(R.string.practice_settings_menu_practice_mode_write)
}

@Preview(showBackground = true)
@Composable
private fun PracticeModeSettingsContentPreview() {
    PracticeModeSettingsContent(
        isSettingsMenuVisible = true,
        currentMode = PracticeMode.Click,
        caseSensitive = false,
        onCaseSensitiveChanged = { _ -> },
        onDismissMenu = {},
        onTestModeChanged = { },
        onToggleMenu = {}
    )
}
