package com.szpejsoft.flashcards.di

import android.app.Application
import androidx.room.Room
import com.szpejsoft.flashcards.Constants
import com.szpejsoft.flashcards.data.db.FlashcardsDb
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
    fun provideRoomDb(app: Application): FlashcardsDb =
        Room.databaseBuilder(
            app,
            FlashcardsDb::class.java,
            Constants.DATABASE_NAME
        ).build()

}