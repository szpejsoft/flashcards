package com.szpejsoft.flashcards.domain.model

data class CardSetWithFlashcards(
    val cardSet: CardSet,
    val flashcards: List<Flashcard>
)
