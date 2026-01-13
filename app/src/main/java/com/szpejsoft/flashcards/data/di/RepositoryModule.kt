package com.szpejsoft.flashcards.data.di

import com.szpejsoft.flashcards.data.db.FlashcardsDb
import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.repository.CardSetRepositoryImpl
import com.szpejsoft.flashcards.data.repository.CardSetWithFlashcardsRepositoryImpl
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCardSetRepository(carSetDao: CardSetDao): CardSetRepository = CardSetRepositoryImpl(carSetDao)

    @Provides
    @Singleton
    fun provideCardSetWithFlashcardsRepository(db: FlashcardsDb): CardSetWithFlashcardsRepository =
        CardSetWithFlashcardsRepositoryImpl(db)

}

