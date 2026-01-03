package com.szpejsoft.flashcards.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.szpejsoft.flashcards.ui.screens.cardsets.add.AddCardSetScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetViewModel
import com.szpejsoft.flashcards.ui.screens.cardsets.list.CardSetListScreen
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.AddCardSet
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.CardSetList
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.EditCardSet

typealias NavEntryProvider = (Screen) -> NavEntry<Screen>

class ScreenNavigator {
    val backStack = mutableStateListOf<Screen>(CardSetList)

    val entryProvider: NavEntryProvider = entryProvider {
        entry<CardSetList> {
            CardSetListScreen(
                onAddButtonClick = { navigateToAddCardSet() },
                onEditButtonClick = { navigateToEditCardSet(it) }
            )

        }
        entry<AddCardSet> {
            AddCardSetScreen(onNavigateBack = { navigateBack() })
        }

        entry<EditCardSet> { key ->
            val viewModel = hiltViewModel<EditCardSetViewModel, EditCardSetViewModel.Factory>(
                creationCallback = { factory -> factory.create(key.id) }
            )
            EditCardSetScreen(
                viewModel = viewModel,
                onNavigateBack = { navigateBack() })
        }
    }

    @Composable
    fun NavDisplay() {
        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider,
            onBack = { navigateBack() }
        )
    }

    private fun navigateToAddCardSet() {
        backStack.add(AddCardSet)
    }

    private fun navigateToEditCardSet(id: Long) {
        backStack.add(EditCardSet(id))
    }

    private fun navigateBack() {
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