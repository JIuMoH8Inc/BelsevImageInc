package com.example.picturegallery.injection.module

import com.example.picturegallery.domain.useCase.DateUseCase
import com.example.picturegallery.domain.useCase.impl.DateUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    @Binds
    abstract fun bindDateUseCase(dateUseCase: DateUseCaseImpl): DateUseCase
}