package com.codebythura.fruit2048.data

import com.codebythura.fruit2048.database.GameStateEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class GameEngineState(
    val gridData: GridMatrix = EMPTY_GRID,
    val score: Int = 0,
) {

    fun toTileData(): List<TileData> {
        return gridData.flatMapIndexed { row, list ->
            list.mapIndexedNotNull { col, data -> data?.intoTile(row, col) }
        }
    }

    fun toEntity() = GameStateEntity(
        state = gridData,
        score = score,
    )

}

abstract class GameEngine {
    abstract val gameState: StateFlow<GameEngineState>
    open fun checkIfGameOver(): Boolean = gameState.value.gridData.checkIfGameOver()
    abstract fun startNewGame()
    abstract fun undo(newState : GameEngineState)
    abstract fun onSwipe(direction : Direction)
}

class GameEngineImpl @Inject constructor() : GameEngine() {

    private val _gameState = MutableStateFlow(GameEngineState())

    override val gameState: StateFlow<GameEngineState>
        get() = _gameState.asStateFlow()

    val gridData: GridMatrix
        get() = _gameState.value.gridData

    override fun startNewGame() {
        _gameState.value = GameEngineState(
            gridData = EMPTY_GRID.addRandomData(count = 2),
            score = 0,
        )
    }

    override fun undo(newState : GameEngineState) {
        _gameState.value = newState
    }

    override fun onSwipe(direction: Direction) {
        val (newGrid,newScore) =  when(direction) {
            Direction.LEFT -> gridData.swipeToLeft()
            Direction.RIGHT -> gridData.swipeToRight()
            Direction.UP -> gridData.swipeUp()
            Direction.DOWN -> gridData.swipeDown()
        }
        _gameState.value = GameEngineState(
            gridData = newGrid.addRandomData(),
            score = _gameState.value.score + newScore
        )
    }

}
