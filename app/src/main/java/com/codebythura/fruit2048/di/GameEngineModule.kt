package com.codebythura.fruit2048.di

import com.codebythura.fruit2048.data.GameEngine
import com.codebythura.fruit2048.data.GameEngineImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class GameEngineModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGameEngine(impl : GameEngineImpl) : GameEngine

}