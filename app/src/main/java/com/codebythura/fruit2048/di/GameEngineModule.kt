package com.codebythura.fruit2048.di

import com.codebythura.fruit2048.data.GameEngine
import com.codebythura.fruit2048.data.GameEngineImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GameEngineModule {

    @Binds
    @Singleton
    abstract fun bindGameEngine(impl : GameEngineImpl) : GameEngine

}