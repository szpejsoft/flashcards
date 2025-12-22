package com.szpejsoft.flashcards.data.di

import com.szpejsoft.flashcards.data.db.dao.CardSetDao
import com.szpejsoft.flashcards.data.repository.CardSetRepositoryImpl
import com.szpejsoft.flashcards.domain.repository.CardSetRepository
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
    fun provideCardSetRepository(cardSetDao: CardSetDao): CardSetRepository = CardSetRepositoryImpl(cardSetDao)
}