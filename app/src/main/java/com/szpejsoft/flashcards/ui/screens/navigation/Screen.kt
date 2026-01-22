package com.szpejsoft.flashcards.ui.screens.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Screen : Parcelable {
    @Parcelize
    data object AddCardSet : Screen()

    @Parcelize
    data object EditCardSetList : Screen()

    @Parcelize
    data class EditCardSet(val id: Long) : Screen()

    @Parcelize
    data class LearnCardSet(val id: Long) : Screen()

    @Parcelize
    data object LearnCardSetList : Screen()

    @Parcelize
    data object TestCardSetList : Screen()

    @Parcelize
    data class TestCardSet(val id: Long) : Screen()

}
