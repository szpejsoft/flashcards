package com.szpejsoft.flashcards.di

import com.szpejsoft.flashcards.common.db.FlashcardsDb
import com.szpejsoft.flashcards.common.db.dao.CardSetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    fun provideDatabaseCardSetDao(db: FlashcardsDb): CardSetDao = db.cardSetDao()

}