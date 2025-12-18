package com.szpejsoft.flashcards.ui.screens.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class Screen : Parcelable {
    @Parcelize
    data object CardSetList: Screen()
    @Parcelize
    data object AddCardSet: Screen()
}