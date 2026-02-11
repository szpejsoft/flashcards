package com.szpejsoft.flashcards.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetViewModelImpl
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModelImpl
import com.szpejsoft.flashcards.presentation.test.TestCardSetViewModelImpl
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.AddCardSetScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetListScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.edit.EditCardSetScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.LearnCardSetListScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.learn.LearnCardSetScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.test.TestCardSetListScreen
import com.szpejsoft.flashcards.ui.screens.cardsets.test.TestCardSetScreen
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.AddCardSet
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.EditCardSet
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.EditCardSetList
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.LearnCardSet
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.LearnCardSetList
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.TestCardSet
import com.szpejsoft.flashcards.ui.screens.navigation.Screen.TestCardSetList
import kotlinx.coroutines.flow.MutableStateFlow

typealias NavEntryProvider = (Screen) -> NavEntry<Screen>

class ScreenNavigator {

    private val backStack = mutableStateListOf<Screen>(LearnCardSetList)
    private val currentTab = MutableStateFlow<BottomTab>(BottomTab.Learn)

    private val entryProvider: NavEntryProvider = entryProvider {

        entry<AddCardSet> {
            AddCardSetScreen(onNavigateBack = { navigateBack() })
        }

        entry<EditCardSetList> {
            EditCardSetListScreen(
                onAddButtonClick = { navigateToAddCardSet() },
                onEditButtonClick = { navigateToEditCardSet(it) })
        }

        entry<EditCardSet> { key ->
            val viewModel = hiltViewModel<EditCardSetViewModelImpl, EditCardSetViewModelImpl.Factory>(
                creationCallback = { factory -> factory.create(key.id) }
            )
            EditCardSetScreen(
                onNavigateBack = { navigateBack() },
                viewModel = viewModel
            )
        }

        entry<LearnCardSetList> {
            LearnCardSetListScreen(onLearnButtonClick = { navigateToLearnCardSet(it) })
        }

        entry<LearnCardSet> { key ->
            val viewModel = hiltViewModel<LearnCardSetViewModelImpl, LearnCardSetViewModelImpl.Factory>(
                creationCallback = { factory -> factory.create(key.id) }
            )
            LearnCardSetScreen(
                onNavigateBack = { navigateBack() },
                viewModel = viewModel
            )
        }

        entry<TestCardSetList> {
            TestCardSetListScreen(
                onTestButtonClick = { navigateToTestCardSet(it) }
            )
        }

        entry<TestCardSet> { key ->
            val viewModel = hiltViewModel<TestCardSetViewModelImpl, TestCardSetViewModelImpl.Factory>(
                creationCallback = { factory -> factory.create(key.id) }
            )
            TestCardSetScreen(
                onNavigateBack = { navigateBack() },
                viewModel = viewModel
            )
        }
    }

    @Composable
    fun NavDisplay(
        modifier: Modifier = Modifier
    ) {
        NavDisplay(
            modifier = modifier,
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider,
            onBack = { navigateBack() }
        )
    }

    @Composable
    fun BottomNavBar(modifier: Modifier = Modifier) {
        BottomNavigationBar(
            modifier = modifier,
            selectedTab = currentTab.collectAsState().value,
            onTabSelected = { tab ->
                onTabSelected(tab)
            }
        )
    }

    private fun onTabSelected(tab: BottomTab) {
        currentTab.value = tab
        val screen = when (tab) {
            BottomTab.Edit -> EditCardSetList
            BottomTab.Learn -> LearnCardSetList
            BottomTab.Test -> TestCardSetList
        }
        backStack.clear()
        backStack.add(screen)
    }

    private fun navigateToAddCardSet() {
        backStack.add(AddCardSet)
    }

    private fun navigateToEditCardSet(id: Long) {
        backStack.add(EditCardSet(id))
    }

    private fun navigateToLearnCardSet(id: Long) {
        backStack.add(LearnCardSet(id))
    }

    private fun navigateToTestCardSet(id: Long) {
        backStack.add(TestCardSet(id))
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
