package com.szpejsoft.flashcards.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.szpejsoft.flashcards.data.db.entities.DbCardSet
import kotlinx.coroutines.flow.Flow

@Dao
interface CardSetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dbCardSet: DbCardSet)

    @Query("SELECT * FROM card_set")
    fun observe(): Flow<List<DbCardSet>>

}