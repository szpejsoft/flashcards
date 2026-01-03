package com.szpejsoft.flashcards.ui.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.szpejsoft.flashcards.ui.screens.navigation.ScreenNavigator
import com.szpejsoft.flashcards.ui.theme.FlashcardsTheme

@Composable
fun MainScreen() {
    val screenNavigator = rememberSaveable(saver = ScreenNavigator.saver) { ScreenNavigator() }

    FlashcardsTheme() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                MainScreenContent(innerPadding, screenNavigator)
            }
        )
    }
}

@Composable
fun MainScreenContent(
    padding: PaddingValues,
    screenNavigator: ScreenNavigator
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 12.dp)
    ) {
        screenNavigator.NavDisplay()

    }
}