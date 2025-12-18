package com.szpejsoft.flashcards.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.szpejsoft.flashcards.common.db.entities.DbCardSet
import kotlinx.coroutines.flow.Flow

@Dao
interface CardSetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dbCardSet: DbCardSet)

    @Query("SELECT * FROM cardset")
    fun observe(): Flow<List<DbCardSet>>

}