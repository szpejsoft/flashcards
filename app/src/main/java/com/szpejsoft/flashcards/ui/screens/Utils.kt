package com.szpejsoft.flashcards.ui.screens

import kotlin.random.Random

fun <T> MutableList<T>.replaceWith(elements: List<T>) {
    clear()
    addAll(elements)
}

fun <T> List<T>.getRandom(): T {
    val index = Random.nextInt(this.size)
    return this[index]
}