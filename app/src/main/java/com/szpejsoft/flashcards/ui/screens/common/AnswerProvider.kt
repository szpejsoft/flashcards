package com.szpejsoft.flashcards.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.szpejsoft.flashcards.R

@Composable
fun AnswerProvider(
    onSkipAnswer: () -> Unit,
    onAnswerProvided: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var answer by remember { mutableStateOf("") }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .weight(1.0f)
                .padding(end = 4.dp)
                .testTag("answerTextField"),

            value = answer,
            onValueChange = { answer = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAnswerProvided(answer)
                    answer = ""
                    keyboardController?.hide()
                }
            )
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

@Preview(showBackground = true)
@Composable
private fun AnswerProviderPreview() {
    AnswerProvider(
        onSkipAnswer = {},
        onAnswerProvided = {},
        modifier = Modifier
    )
}