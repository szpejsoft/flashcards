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
import androidx.compose.ui.unit.dp
import com.szpejsoft.flashcards.R

@Composable
fun LearningProgress(
    learned: Int,
    setSize: Int,
    modifier: Modifier = Modifier
) {
    val ratio = if (setSize == 0) 0f else learned.toFloat() / setSize.toFloat()

    Column(
        modifier = modifier.fillMaxWidth(),
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
                .clip(RoundedCornerShape(6.dp))
                .height(12.dp)
                .background(MaterialTheme.colorScheme.primaryFixed)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio)
                    .clip(RoundedCornerShape(6.dp))
                    .height(12.dp)
                    .background(MaterialTheme.colorScheme.onPrimaryFixed)
                    .align(Alignment.CenterStart)
            )
        }
    }
}
