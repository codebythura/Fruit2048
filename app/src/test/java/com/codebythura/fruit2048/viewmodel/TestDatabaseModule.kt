package com.codebythura.fruit2048.viewmodel

import android.content.Context
import androidx.room.Room
import com.codebythura.fruit2048.database.AppDatabase
import com.codebythura.fruit2048.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .allowMainThreadQueries()
            .build()
    }

}