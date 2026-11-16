package com.codebythura.fruit2048.repository

import androidx.datastore.core.DataStore
import com.codebythura.fruit2048.AppData
import com.codebythura.fruit2048.copy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DataStoreRepository {
    fun observeBestScore(): Flow<Int>
    suspend fun updateBestScore(newScore: Int) : Boolean
}

class DataStoreRepositoryImpl @Inject constructor(private val datastore: DataStore<AppData>) :
    DataStoreRepository {

    override fun observeBestScore() = datastore.data.map { it.bestScore }.flowOn(Dispatchers.IO)

    override suspend fun updateBestScore(newScore: Int): Boolean =
        withContext(Dispatchers.IO) {
            var updated = false
            datastore.updateData { currentData ->
                currentData.copy {
                    if (newScore > bestScore) {
                        bestScore = newScore
                        updated = true
                    }
                }
            }
            return@withContext updated
        }


}