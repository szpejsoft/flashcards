package com.szpejsoft.flashcards.common.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cardset")
data class DbCardSet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdDate: String //in ISO 8601 format i.e. YYYY-MM-DDTHH:mm:ss.sssZ
)