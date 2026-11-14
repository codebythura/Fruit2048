package com.codebythura.fruit2048.ui.game

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.data.EMPTY_GRID
import com.codebythura.fruit2048.data.GameLogic
import com.codebythura.fruit2048.data.TileData
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
    val isGameOver: Boolean = false,
    val undoEnabled: Boolean = false,
    val showNewScoreDialog : Boolean = false,
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameStateRepo: GameStateRepository,
    private val dataStoreRepo: DataStoreRepository,
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val uiState = combine(
        _gameState,
        gameStateRepo.observeRowCount(),
        dataStoreRepo.observeBestScore()
    ) { gameState, lastState, bestScore ->
        GameUIState(
            tileData = gameState.toTileData(),
            score = gameState.score,
            bestScore = bestScore,
            isGameOver = GameLogic.checkIfGameOver(gameState.gridData),
            undoEnabled = lastState > 0,
            showNewScoreDialog = gameState.showNewScoreDialog,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GameUIState()
    )

    init {
        startNewGame()
    }

    fun startNewGame() {
        viewModelScope.launch {
            gameStateRepo.deleteAllStates() // delete all game state from db
            val showNewScoreDialog = dataStoreRepo.updateBestScoreIfNecessary(_gameState.value.score)
            val initialGrid = GameLogic.addRandomData(EMPTY_GRID, count = 2)
            _gameState.value = GameState(
                gridData = initialGrid,
                score = 0,
                showNewScoreDialog = showNewScoreDialog,
            )
        }
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
                Direction.LEFT -> GameLogic.swipeToLeft(_gameState.value.gridData)
                Direction.RIGHT -> GameLogic.swipeToRight(_gameState.value.gridData)
                Direction.UP -> GameLogic.swipeUp(_gameState.value.gridData)
                Direction.DOWN -> GameLogic.swipeDown(_gameState.value.gridData)
            }
            _gameState.value = GameState(
                gridData = GameLogic.addRandomData(swipedGrid),
                score = _gameState.value.score + newScore
            )
        }
    }

}
