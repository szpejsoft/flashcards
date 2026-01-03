package com.szpejsoft.flashcards.data.di

import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.db.dao.FlashcardDao
import com.szpejsoft.flashcards.data.repository.CardSetRepositoryImpl
import com.szpejsoft.flashcards.data.repository.CardSetWithFlashcardsRepositoryImpl
import com.szpejsoft.flashcards.data.repository.FlashcardRepositoryImpl
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
import com.szpejsoft.flashcards.domain.repository.CardSetWithFlashcardsRepository
import com.szpejsoft.flashcards.domain.repository.FlashcardRepository
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
    fun provideCardSetRepository(cardSetDao: CardSetDao): CardSetRepository =
        CardSetRepositoryImpl(cardSetDao)

    @Provides
    @Singleton
    fun provideCardSetWithFlashcardsRepository(cardSetDao: CardSetDao): CardSetWithFlashcardsRepository =
        CardSetWithFlashcardsRepositoryImpl(cardSetDao)

    @Provides
    @Singleton
    fun provideFlashcardRepository(flashcardDao: FlashcardDao): FlashcardRepository =
        FlashcardRepositoryImpl(flashcardDao)

}
