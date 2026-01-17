package com.szpejsoft.flashcards.ui.screens.cardsets.learn


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.szpejsoft.flashcards.R

@Preview
@Composable
fun LearningProgress(
    learned: Int = 1,
    setSize: Int = 2
) {
    val ratio = if (setSize == 0) 0f else learned.toFloat() / setSize.toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .testTag("learningProgress"),
            text = stringResource(R.string.progress, learned, setSize),
            textAlign = TextAlign.Start,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .height(24.dp)
                .background(MaterialTheme.colorScheme.primaryFixed)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio)
                    .clip(RoundedCornerShape(12.dp))
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.onPrimaryFixed)
                    .align(Alignment.CenterStart)
            )
        }
    }
}