package com.szpejsoft.flashcards.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random


fun <T> List<T>.getRandom(): T {
    val index = Random.nextInt(this.size)
    return this[index]
}

fun <T> MutableCollection<T>.replaceWith(values: Collection<T>) {
    clear()
    addAll(values)
}
