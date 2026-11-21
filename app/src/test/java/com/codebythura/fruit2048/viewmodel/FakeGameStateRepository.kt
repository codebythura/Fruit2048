package com.codebythura.fruit2048.viewmodel

import com.codebythura.fruit2048.database.GameStateEntity
import com.codebythura.fruit2048.repository.GameStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map


class FakeGameStateRepository : GameStateRepository {

    private val savedStates = MutableStateFlow<List<GameStateEntity>>(emptyList())
    private var undoGameResult : GameStateEntity? = null
    private var resumeGameResult : GameStateEntity? = null

    override fun observeRowCount(): Flow<Int> = savedStates.map { it.size }

    override suspend fun getRowCount(): Int = savedStates.value.size

    override suspend fun onUndo(): GameStateEntity? = undoGameResult

    override suspend fun onResume(): GameStateEntity? = resumeGameResult

    override suspend fun saveState(state: GameStateEntity) {
        savedStates.value += state
    }

    override suspend fun deleteAllStates() {
        savedStates.value = emptyList()
    }

    fun setSavedStates(states : List<GameStateEntity>) {
        savedStates.value = states
    }



}