package com.szpejsoft.flashcards.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun BoxScope.ScreenBackground(
    imageVector: ImageVector,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier
            .align(Alignment.Center)
            .fillMaxSize(0.62f)
            .graphicsLayer(alpha = 0.05f),
        tint = MaterialTheme.colorScheme.primary
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenBackgroundPreview() {
    Box() {
        ScreenBackground(imageVector = Icons.Outlined.Edit)
    }
}