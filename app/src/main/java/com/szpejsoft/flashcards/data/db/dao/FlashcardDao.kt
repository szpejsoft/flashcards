package com.szpejsoft.flashcards.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.szpejsoft.flashcards.data.db.entities.DbFlashcard

@Dao
interface FlashcardDao {

    @Query("DELETE FROM flash_card WHERE id IN (:ids)")
    suspend fun delete(ids: List<Long>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(dbCardSets: List<DbFlashcard>)

}
