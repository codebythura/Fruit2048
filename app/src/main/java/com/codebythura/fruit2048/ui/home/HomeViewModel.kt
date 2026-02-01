package com.codebythura.fruit2048.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.data.GameLogic
import com.codebythura.fruit2048.data.GridMatrix
import com.codebythura.fruit2048.data.TileData
import com.codebythura.fruit2048.data.EMPTY_GRID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class GameUIState(
    val tileData: List<TileData> = emptyList(),
    val score: Int = 0,
    val isGameOver: Boolean = false,
)

private data class GameState(
    val gridData: GridMatrix = EMPTY_GRID,
    val score: Int = 0,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as GameState
        if (score != other.score) return false
        if (!gridData.contentDeepEquals(other.gridData)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = score
        result = 31 * result + gridData.contentDeepHashCode()
        return result
    }

}

class HomeViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameUIState: StateFlow<GameUIState> = _gameState
        .map { state -> mapGameStateToUIState(state) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GameUIState()
        )

    init {
        startNewGame()
    }

    fun startNewGame() {
        _gameState.update {
            val initialGrid = GameLogic.addRandomData(EMPTY_GRID, count = 2)
            GameState(gridData = initialGrid, score = 0)
        }
    }

    private fun mapGameStateToUIState(gameState: GameState): GameUIState {
        val tileData = gameState.gridData.flatMapIndexed { row, list ->
            list.mapIndexedNotNull { col, data -> data?.intoTile(row, col) }
        }
        return GameUIState(
            tileData = tileData,
            score = gameState.score,
            isGameOver = GameLogic.checkIfGameOver(gameState.gridData)
        )
    }

    fun onSwipe(direction: Direction) {
        val (swipedGrid, newScore) = when (direction) {
            Direction.LEFT -> GameLogic.swipeToLeft(_gameState.value.gridData)
            Direction.RIGHT -> GameLogic.swipeToRight(_gameState.value.gridData)
            Direction.UP -> GameLogic.swipeUp(_gameState.value.gridData)
            Direction.DOWN -> GameLogic.swipeDown(_gameState.value.gridData)
        }
        _gameState.update { currentState ->
            currentState.copy(
                gridData = GameLogic.addRandomData(swipedGrid),
                score = currentState.score + newScore
            )
        }
    }

}
