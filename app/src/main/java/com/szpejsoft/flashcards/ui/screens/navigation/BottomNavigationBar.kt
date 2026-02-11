package com.szpejsoft.flashcards.ui.screens.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.szpejsoft.flashcards.R

@Composable
fun BottomNavigationBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth()
    ) {
        bottomNavigationItems.forEach { (tab, item) ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            )
        }
    }
}

val bottomNavigationItems
    @Composable
    get() = mapOf(
        BottomTab.Learn to BottomNavigationItem(
            stringResource(R.string.bottom_nav_item_learn),
            Icons.Default.Book
        ),
        BottomTab.Test to BottomNavigationItem(
            stringResource(R.string.bottom_nav_item_test),
            Icons.Default.Leaderboard
        ),
        BottomTab.Edit to BottomNavigationItem(
            stringResource(R.string.bottom_nav_item_edit),
            Icons.Default.Edit
        ),
    )

sealed class BottomTab {
    data object Learn : BottomTab()
    data object Test : BottomTab()
    data object Edit : BottomTab()
}

data class BottomNavigationItem(
    val label: String,
    val icon: ImageVector
)
