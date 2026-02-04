package com.szpejsoft.flashcards.presentation.di

import com.szpejsoft.flashcards.presentation.cardsets.AddCardSetViewModel
import com.szpejsoft.flashcards.presentation.cardsets.AddCardSetViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class) // Scoped to ViewModels
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindAddCardSetViewModel(
        impl: AddCardSetViewModelImpl
    ): AddCardSetViewModel

}