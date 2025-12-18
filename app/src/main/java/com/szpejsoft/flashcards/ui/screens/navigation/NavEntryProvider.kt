package com.szpejsoft.flashcards.ui.screens.navigation

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import com.szpejsoft.flashcards.ui.screens.cardsets.AddCardSetScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.CardSetListScreen
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.AddCardSet
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.CardSetList

typealias NavEntryProvider = (Screen) -> NavEntry<Screen>

