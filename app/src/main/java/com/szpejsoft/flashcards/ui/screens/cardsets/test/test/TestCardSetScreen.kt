package com.szpejsoft.flashcards.ui.screens.cardsets.test.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.szpejsoft.flashcards.R
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.TestMode
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState.FlashcardToTest
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModel.UiState.TestFinished
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModelImpl

@Composable
fun TestCardSetScreen(
    viewModel: TestCardSetViewModel = hiltViewModel<TestCardSetViewModelImpl>(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is FlashcardToTest -> TestCardSetContent(
            state as FlashcardToTest,
            viewModel::onCardLearned,
            viewModel::onCardNotLearned,
            viewModel::onAnswerProvided,
            viewModel::onTestModeChanged,
            viewModel::onCaseSensitiveChanged
        )

        is TestFinished -> LearningFinished(
            state as TestFinished,
            onNavigateBack
        )
    }
}

@Composable
fun TestCardSetContent(
    state: FlashcardToTest,
    onCardLearned: () -> Unit,
    onCardNotLearned: () -> Unit,
    onAnswerProvided: (String) -> Unit,
    onTestModeChanged: (TestMode) -> Unit,
    onCaseSensitiveChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                text = state.setName,
            )
            SettingsButtonWithMenu(
                currentMode = state.testMode,
                caseSensitive = state.caseSensitive,
                onTestModeChanged = onTestModeChanged,
                onCaseSensitiveChanged = onCaseSensitiveChanged
            )
        }
        TestProgress(
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 12.dp
            ),
            learned = state.learnedCards,
            failed = state.failedCards,
            setSize = state.cardSetSize
        )
        Spacer(modifier = Modifier.weight(0.19f))
        Flashcard(state.flashcardToTest.obverse, Modifier.weight(0.62f))
        Spacer(modifier = Modifier.weight(0.19f))
        if (state.testMode == TestMode.Click) {
            Buttons(onCardNotLearned, onCardLearned)
        } else {
            AnswerProvider(onCardNotLearned, onAnswerProvided)
        }
    }
}

@Composable
private fun AnswerProvider(
    onSkipAnswer: () -> Unit,
    onAnswerProvided: (String) -> Unit
) {
    var answer by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            value = answer,
            onValueChange = { answer = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAnswerProvided(answer) })
        )
        Button(
            modifier = Modifier
                .testTag("cardNotLearnedButton"),
            onClick = onSkipAnswer
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.wcag_button_not_learned_description)
            )
        }
    }
}

@Composable
private fun SettingsButtonWithMenu(
    currentMode: TestMode,
    caseSensitive: Boolean,
    onTestModeChanged: (TestMode) -> Unit,
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
                TestModeMenuItem(
                    testMode = TestMode.Click,
                    isSelected = TestMode.Click == currentMode,
                    onTestModeChanged = { onTestModeChanged(TestMode.Click); isSettingsMenuVisible = false }
                )
                HorizontalDivider()
                TestModeMenuItem(
                    testMode = TestMode.Write,
                    isSelected = TestMode.Write == currentMode,
                    onTestModeChanged = { onTestModeChanged(TestMode.Write) }
                )
                DropdownMenuItem(
                    modifier = Modifier
                        .height(56.dp)
                        .align(Alignment.Start),
                    enabled = currentMode == TestMode.Write,
                    onClick = { },
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                text = stringResource(R.string.test_card_set_screen_settings_case_sensitive),
                                textAlign = TextAlign.Start
                            )
                            Switch(
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
private fun ColumnScope.TestModeMenuItem(
    testMode: TestMode,
    isSelected: Boolean,
    onTestModeChanged: (TestMode) -> Unit
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
                        .weight(1f)
                        .padding(end = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    text = testMode.getName(),
                    textAlign = TextAlign.Start
                )
                RadioButton(
                    selected = isSelected,
                    onClick = {
                        onTestModeChanged(testMode)
                    }
                )
            }
        }
    )
}


@Composable
private fun Buttons(
    onCardNotLearned: () -> Unit,
    onCardLearned: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            modifier = Modifier
                .weight(0.5f)
                .testTag("cardNotLearnedButton"),
            onClick = onCardNotLearned
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.wcag_button_not_learned_description)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            modifier = Modifier
                .weight(0.5f)
                .testTag("cardLearnedButton"),
            onClick = onCardLearned
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.wcag_button_learned_description)
            )
        }
    }
}

@Composable
private fun LearningFinished(
    state: TestFinished,
    onBackButtonClicked: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.test_card_set_screen_test_finished_title),
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(
                        R.string.test_card_set_screen_test_finished_score,
                        state.learnedCards,
                        state.cardSetSize
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBackButtonClicked
        ) {
            Text(stringResource(R.string.learn_card_set_screen_learning_go_to_card_set_list))
        }
    }
}

@Composable
private fun TestMode.getName(): String = when (this) {
    TestMode.Click -> stringResource(R.string.test_card_set_screen_settings_test_mode_click)
    TestMode.Write -> stringResource(R.string.test_card_set_screen_settings_test_mode_write)
}
