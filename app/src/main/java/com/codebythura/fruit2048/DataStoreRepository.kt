package com.codebythura.fruit2048

import androidx.datastore.core.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DataStoreRepository {
    fun observeBestScore(): Flow<Int>
    suspend fun updateBestScoreIfNecessary(newScore: Int)
}

class DataStoreRepositoryImpl @Inject constructor(private val datastore: DataStore<AppData>) : DataStoreRepository {

    override fun observeBestScore() = datastore.data.map { it.bestScore }.flowOn(Dispatchers.IO)

    override suspend fun updateBestScoreIfNecessary(newScore: Int): Unit = withContext(Dispatchers.IO) {
        datastore.updateData { currentData ->
            currentData.copy { bestScore = maxOf(newScore,bestScore) }
        }
    }


}