package com.szpejsoft.flashcards.ui.screens.cardsets.learn

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
internal fun FlippableFlashCard(
    obverse: String,
    reverse: String,
    isFlippable: Boolean,
    modifier: Modifier = Modifier
) {
    var showObverse by remember("$obverse $reverse") { mutableStateOf(true) }
    val angle = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val currentText = if (angle.value < 90f) obverse else reverse

    LaunchedEffect(showObverse) {
        val targetAngle = if (showObverse) 0f else 180f
        if (angle.value == targetAngle) return@LaunchedEffect

        val duration = 500
        launch {
            angle.animateTo(
                targetValue = targetAngle,
                animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing)
            )
        }
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = keyframes {
                    durationMillis = duration
                    1.0f at 0
                    0.3f at duration / 2
                    1.0f at duration
                }
            )
        }
    }

    Card(
        modifier = modifier
            .graphicsLayer {
                rotationX = angle.value
                scaleX = scale.value
                scaleY = scale.value
            },
        onClick = {
            if (isFlippable) {
                showObverse = !showObverse
            }
        },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        rotationX = angle.value
                    },
                textAlign = TextAlign.Center,
                text = currentText,
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlippableFlashCardPreview() {
    FlippableFlashCard(
        obverse = "question",
        reverse = "answer",
        isFlippable = true,
        modifier = Modifier
    )
}