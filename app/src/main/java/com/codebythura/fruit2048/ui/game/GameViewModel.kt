package com.codebythura.fruit2048.ui.game

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.data.EMPTY_GRID
import com.codebythura.fruit2048.data.GameEngine
import com.codebythura.fruit2048.data.GameLogic
import com.codebythura.fruit2048.data.TileData
import com.codebythura.fruit2048.data.swipeDown
import com.codebythura.fruit2048.data.swipeToLeft
import com.codebythura.fruit2048.data.swipeToRight
import com.codebythura.fruit2048.data.swipeUp
import com.codebythura.fruit2048.database.toState
import com.codebythura.fruit2048.repository.DataStoreRepository
import com.codebythura.fruit2048.repository.GameStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class GameUIState(
    val tileData: List<TileData> = emptyList(),
    val bestScore: Int = 0,
    val score: Int = 0,
    val undoEnabled: Boolean = false,
    val showGameOverDialog: Boolean = false,
    val showNewScoreDialog : Boolean = false,
    val showRestartConfirmDialog : Boolean = false,
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameStateRepo: GameStateRepository,
    private val dataStoreRepo: DataStoreRepository,
    private val gameEngine: GameEngine,
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val uiState = combine(
        _gameState,
        gameStateRepo.observeRowCount(),
        dataStoreRepo.observeBestScore(),
    ) { gameState, lastState, bestScore ->
        GameUIState(
            tileData = gameState.toTileData(),
            score = gameState.score,
            bestScore = bestScore,
            undoEnabled = lastState > 0,
            showGameOverDialog = GameLogic.checkIfGameOver(gameState.gridData),
            showNewScoreDialog = gameState.showNewScoreDialog,
            showRestartConfirmDialog = gameState.showRestartConfirmDialog
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GameUIState()
    )

    init {
        startNewGame()
    }

    fun onRestartGame() {
        if(uiState.value.undoEnabled) {
            _gameState.update { it.copy(showRestartConfirmDialog = true) }
        } else {
            startNewGame()
        }
    }

    fun startNewGame() {
        viewModelScope.launch {
            gameStateRepo.deleteAllStates() // delete all game state from db
            val showNewScoreDialog = dataStoreRepo.updateBestScore(_gameState.value.score)
            val initialGrid = GameLogic.addRandomData(EMPTY_GRID, count = 2)
            _gameState.value = GameState(
                gridData = initialGrid,
                score = 0,
                showNewScoreDialog = showNewScoreDialog,
            )
        }
    }

    fun onRestartGameDialogDismiss() {
        _gameState.update { it.copy(showRestartConfirmDialog = false) }
    }

    fun onNewScoreDialogDismiss() {
        _gameState.update { it.copy(showNewScoreDialog = false) }
    }

    fun onUndo() {
        viewModelScope.launch {
            gameStateRepo.getAndDeleteLastState()?.let { stateFromDb ->
                _gameState.value = stateFromDb.toState()
            }
        }
    }

    fun onSwipe(direction: Direction) {
        viewModelScope.launch {
            gameStateRepo.saveState(_gameState.value.toEntity()) // save the current state first
            val (swipedGrid, newScore) = when (direction) {
                Direction.LEFT -> _gameState.value.gridData.swipeToLeft()
                Direction.RIGHT -> _gameState.value.gridData.swipeToRight()
                Direction.UP -> _gameState.value.gridData.swipeUp()
                Direction.DOWN -> _gameState.value.gridData.swipeDown()
            }
            _gameState.value = GameState(
                gridData = GameLogic.addRandomData(swipedGrid),
                score = _gameState.value.score + newScore
            )
        }
    }

}
