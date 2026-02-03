package com.codebythura.fruit2048.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.codebythura.fruit2048.DataStoreRepository
import com.codebythura.fruit2048.DataStoreRepositoryImpl
import com.codebythura.fruit2048.GameStateRepository
import com.codebythura.fruit2048.GameStateRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGameStateRepository(impl: GameStateRepositoryImpl): GameStateRepository

    @Binds
    @Singleton
    abstract fun bindDataStoreRepository(impl: DataStoreRepositoryImpl): DataStoreRepository

}