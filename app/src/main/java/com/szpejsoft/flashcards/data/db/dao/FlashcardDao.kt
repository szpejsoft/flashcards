package com.szpejsoft.flashcards.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.szpejsoft.flashcards.data.db.entities.DbFlashcard

@Dao
interface FlashcardDao {
    @Query("DELETE FROM flash_card WHERE id = :id")
    suspend fun delete(id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dbCardSet: DbFlashcard)

    @Query("UPDATE flash_card SET obverse = :obverse, reverse = :reverse WHERE id = :id")
    suspend fun update(id: Long, obverse: String, reverse: String)

}