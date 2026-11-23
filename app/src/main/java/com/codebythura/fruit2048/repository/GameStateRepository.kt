package com.codebythura.fruit2048.repository

import com.codebythura.fruit2048.database.AppDatabase
import com.codebythura.fruit2048.database.GameStateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GameStateRepository {
    fun observeRowCount(): Flow<Int>
    suspend fun onUndo(): GameStateEntity?
    suspend fun onResume(): GameStateEntity?
    suspend fun saveState(state: GameStateEntity)
    suspend fun deleteAllStates()
    suspend fun getRowCount() : Int
}

class GameStateRepositoryImpl @Inject constructor(appDatabase: AppDatabase) : GameStateRepository {

    val gameStateDao = appDatabase.gameStateDao

    override suspend fun getRowCount() = observeRowCount().first()

    override fun observeRowCount() = gameStateDao.observeRowCount().flowOn(Dispatchers.IO)

    override suspend fun onUndo() = withContext(Dispatchers.IO) {
        gameStateDao.deleteAndGetLatestState()
    }

    override suspend fun onResume() = withContext(Dispatchers.IO) { gameStateDao.getLatestState() }

    override suspend fun saveState(state: GameStateEntity) = withContext(Dispatchers.IO) {
        gameStateDao.insertGameState(state)
    }

    override suspend fun deleteAllStates() = withContext(Dispatchers.IO) {
        gameStateDao.deleteAllGameStates()
    }

}