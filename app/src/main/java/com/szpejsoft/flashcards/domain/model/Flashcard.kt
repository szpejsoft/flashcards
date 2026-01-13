package com.szpejsoft.flashcards.domain.model

data class Flashcard(
    val id: Long = 0, //0 means new card
    val obverse: String = "",
    val reverse: String = "",
)
