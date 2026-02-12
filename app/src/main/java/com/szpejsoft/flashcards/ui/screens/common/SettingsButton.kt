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
        onToggleMenu = { isSettingsMenuVisible = !isSettingsMenuVisible },
        onDismissMenu = { isSettingsMenuVisible = false },
        currentMode = currentMode,
        onTestModeChanged = onTestModeChanged,
        caseSensitive = caseSensitive,
        onCaseSensitiveChanged = onCaseSensitiveChanged,
        modifier = modifier
    )
}

@Composable
private fun PracticeModeSettingsContent(
    isSettingsMenuVisible: Boolean,
    onToggleMenu: () -> Unit,
    onDismissMenu: () -> Unit,
    currentMode: PracticeMode,
    onTestModeChanged: (PracticeMode) -> Unit,
    caseSensitive: Boolean,
    onCaseSensitiveChanged: (Boolean) -> Unit,
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
                        .align(Alignment.Start),
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
                                modifier = Modifier.testTag("caseSensitiveSwitch"),
                                enabled = currentMode == PracticeMode.Write,
                                checked = caseSensitive,
                                onCheckedChange = null // Click handled by DropdownMenuItem
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
        modifier = modifier.align(Alignment.Start),
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
                    modifier = Modifier.testTag(practiceMode.getName()),
                    selected = isSelected,
                    onClick = null // Click handled by DropdownMenuItem
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
        onToggleMenu = {},
        onDismissMenu = {},
        currentMode = PracticeMode.Click,
        onTestModeChanged = { },
        caseSensitive = false,
        onCaseSensitiveChanged = { _ -> }
    )
}
