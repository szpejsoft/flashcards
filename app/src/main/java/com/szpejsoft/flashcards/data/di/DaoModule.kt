package com.szpejsoft.flashcards.data.di

import com.szpejsoft.flashcards.data.db.FlashcardsDb
import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun provideDatabaseCardSetDao(db: FlashcardsDb): CardSetDao = db.cardSetDao()

}