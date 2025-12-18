package com.szpejsoft.flashcards.ui.screens.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.navigation3.runtime.entryProvider
import com.szpejsoft.flashcards.ui.screens.cardsets.AddCardSetScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.CardSetListScreen
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.AddCardSet
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.CardSetList

class ScreenNavigator {
    val backStack = mutableStateListOf<Screen>(CardSetList)
    val entryProvider: NavEntryProvider = entryProvider {
        entry<CardSetList> { CardSetListScreen { navigateToAddCardSet() } }
        entry<AddCardSet> { AddCardSetScreen { navigateBack() } }
    }

    fun navigateToAddCardSet() {
        backStack.add(AddCardSet)
    }

    fun navigateBack() {
        backStack.removeLastOrNull()
    }

    companion object {
        val saver = listSaver<ScreenNavigator, Screen>(
            save = { navigator -> navigator.backStack.toList() },
            restore = { savedBackStack ->
                ScreenNavigator().apply {
                    backStack.apply {
                        clear()
                        addAll(savedBackStack)
                    }
                }
            }
        )
    }
}