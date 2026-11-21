package com.codebythura.fruit2048.viewmodel

import com.codebythura.fruit2048.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn

class FakeGameDataStoreRepository : DataStoreRepository {

    private val bestScoreFlow = MutableStateFlow(0)

    override fun observeBestScore(): Flow<Int> = bestScoreFlow.asStateFlow()

    override suspend fun updateBestScore(newScore: Int): Boolean {
        if(newScore > bestScoreFlow.value) {
            bestScoreFlow.value = newScore
            return true
        }
        return false
    }

    fun resetBestScore() {
        bestScoreFlow.value = 0
    }

}