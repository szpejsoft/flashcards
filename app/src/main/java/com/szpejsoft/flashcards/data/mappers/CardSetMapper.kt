package com.szpejsoft.flashcards.data.mappers

import com.szpejsoft.flashcards.data.db.entities.DbCardSet
import com.szpejsoft.flashcards.domain.model.CardSet

fun DbCardSet.toDomain() = CardSet(id = id, name = name)

fun List<DbCardSet>.toDomain() = map { it.toDomain() }
