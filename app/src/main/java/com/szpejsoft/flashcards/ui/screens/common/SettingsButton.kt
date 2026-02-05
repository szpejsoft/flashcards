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
import androidx.compose.ui.unit.dp
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.domain.model.PracticeMode


@Composable
fun PracticeModeSettings(
    currentMode: PracticeMode,
    caseSensitive: Boolean,
    onTestModeChanged: (PracticeMode) -> Unit,
    onCaseSensitiveChanged: (Boolean) -> Unit
) {
    var isSettingsMenuVisible by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { isSettingsMenuVisible = !isSettingsMenuVisible }
        ) {
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
                onDismissRequest = { isSettingsMenuVisible = false }
            ) {
                PracticeModeMenuItem(
                    practiceMode = PracticeMode.Click,
                    isSelected = PracticeMode.Click == currentMode,
                    onTestModeChanged = { onTestModeChanged(PracticeMode.Click) }
                )
                HorizontalDivider()
                PracticeModeMenuItem(
                    practiceMode = PracticeMode.Write,
                    isSelected = PracticeMode.Write == currentMode,
                    onTestModeChanged = { onTestModeChanged(PracticeMode.Write) }
                )
                DropdownMenuItem(
                    modifier = Modifier
                        .height(56.dp)
                        .align(Alignment.Start),
                    enabled = currentMode == PracticeMode.Write,
                    onClick = { },
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
                                text = stringResource(R.string.test_card_set_screen_settings_case_sensitive),
                                textAlign = TextAlign.Start,
                            )
                            Switch(
                                modifier = Modifier.testTag("caseSensitiveSwitch"),
                                enabled = currentMode == PracticeMode.Write,
                                checked = caseSensitive,
                                onCheckedChange = { onCaseSensitiveChanged(it) }
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
    onTestModeChanged: (PracticeMode) -> Unit
) {
    DropdownMenuItem(
        modifier = Modifier
            .height(56.dp)
            .align(Alignment.Start),
        onClick = { },
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
                    onClick = {
                        onTestModeChanged(practiceMode)
                    }
                )
            }
        }
    )
}

@Composable
private fun PracticeMode.getName(): String = when (this) {
    PracticeMode.Click -> stringResource(R.string.test_card_set_screen_settings_test_mode_click)
    PracticeMode.Write -> stringResource(R.string.test_card_set_screen_settings_test_mode_write)
}
