package com.szpejsoft.flashcards.di

import android.app.Application
import androidx.room.Room
import com.szpejsoft.flashcards.Constants
import com.szpejsoft.flashcards.data.db.FlashcardsDb
import com.szpejsoft.flashcards.data.db.FlashcardsDbDefinition
import com.szpejsoft.flashcards.data.db.RoomFlashcardsDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): FlashcardsDb {
        val dbDef = Room.databaseBuilder(
            app,
            FlashcardsDbDefinition::class.java,
            Constants.DATABASE_NAME
        ).build()
        return RoomFlashcardsDb(dbDef)
    }
}