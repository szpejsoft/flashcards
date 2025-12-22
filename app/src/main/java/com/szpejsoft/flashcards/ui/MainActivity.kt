package com.szpejsoft.flashcards.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.szpejsoft.flashcards.ui.screens.main.MainScreen
import com.szpejsoft.flashcards.ui.theme.FlashcardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashcardsTheme {
                MainScreen()
            }
        }
    }
}

