package com.codebythura.fruit2048.ui


import com.codebythura.fruit2048.di.RepositoryModule
import com.codebythura.fruit2048.repository.DataStoreRepository
import com.codebythura.fruit2048.repository.GameStateRepository
import com.codebythura.fruit2048.repository.GameStateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeDataStoreModule {

    @Binds
    @Singleton
    abstract fun provideFakeDataStore(fake: FakeDataStoreRepository) : DataStoreRepository

    @Binds
    @Singleton
    abstract fun bindGameStateRepository(impl: GameStateRepositoryImpl) : GameStateRepository

}


@Singleton
class FakeDataStoreRepository @Inject constructor() : DataStoreRepository {

    private val bestScoreFlow = MutableStateFlow(0)

    override fun observeBestScore(): Flow<Int> = bestScoreFlow.asStateFlow()

    override suspend fun updateBestScore(newScore: Int): Boolean {
        if(newScore > bestScoreFlow.value) {
            bestScoreFlow.value = newScore
            return true
        }
        return false
    }

    fun setBestScore(newScore : Int) {
        bestScoreFlow.value = newScore
    }

    fun resetBestScore() {
        bestScoreFlow.value = 0
    }
}