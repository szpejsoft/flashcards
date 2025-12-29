package com.szpejsoft.flashcards.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.szpejsoft.flashcards.data.db.entities.DbCardSet
import com.szpejsoft.flashcards.data.db.entities.DbCardSetWithFlashcards
import kotlinx.coroutines.flow.Flow

@Dao
interface CardSetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dbCardSet: DbCardSet)

    @Query("DELETE FROM card_set WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM card_set")
    fun observeAll(): Flow<List<DbCardSet>>

    @Transaction
    @Query("SELECT * FROM card_set WHERE id = :cardSetId")
    fun observeCardSetWithFlashcards(cardSetId: Long): Flow<DbCardSetWithFlashcards>

}