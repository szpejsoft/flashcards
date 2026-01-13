package com.szpejsoft.flashcards.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "flash_card",
    foreignKeys = [
        ForeignKey(
            entity = DbCardSet::class,
            parentColumns = ["id"],
            childColumns = ["cardSetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("cardSetId")
    ]
)
data class DbFlashcard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardSetId: Long,
    val obverse: String,
    val reverse: String,
)
