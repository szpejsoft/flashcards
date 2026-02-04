package com.szpejsoft.flashcards.presentation.di

import com.szpejsoft.flashcards.presentation.cardsets.AddCardSetViewModel
import com.szpejsoft.flashcards.presentation.cardsets.AddCardSetViewModelImpl
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetListViewModel
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetListViewModelImpl
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetViewModel
import com.szpejsoft.flashcards.presentation.cardsets.EditCardSetViewModelImpl
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetListViewModel
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetListViewModelImpl
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModel
import com.szpejsoft.flashcards.presentation.learn.LearnCardSetViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    // region viewModels
    @Binds
    @ViewModelScoped
    abstract fun bindAddCardSetViewModel(
        impl: AddCardSetViewModelImpl
    ): AddCardSetViewModel

    @Binds
    @ViewModelScoped
    abstract fun bindEditCardSetListViewModel(
        impl: EditCardSetListViewModelImpl
    ): EditCardSetListViewModel

    @Binds
    @ViewModelScoped
    abstract fun bindLearnCardSetListViewModel(
        impl: LearnCardSetListViewModelImpl
    ): LearnCardSetListViewModel
    // endregion viewModels

    //region factories
    @Binds
    abstract fun bindEditCardSetViewModelFactory(
        impl: EditCardSetViewModelImpl.Factory
    ): EditCardSetViewModel.Factory

    @Binds
    abstract fun bindLearnCardSetViewModelFactory(
        impl: LearnCardSetViewModelImpl.Factory
    ): LearnCardSetViewModel.Factory
    //endregion factories
}