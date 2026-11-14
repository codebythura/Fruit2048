package com.codebythura.fruit2048.ui.game

import com.codebythura.fruit2048.data.EMPTY_GRID
import com.codebythura.fruit2048.data.GridMatrix
import com.codebythura.fruit2048.data.TileData
import com.codebythura.fruit2048.database.GameStateEntity

data class GameState(
    val gridData: GridMatrix = EMPTY_GRID,
    val score: Int = 0,
    val showNewScoreDialog: Boolean = false,
    val showRestartConfirmDialog : Boolean = false,
) {
    fun toTileData(): List<TileData> {
        return gridData.flatMapIndexed { row, list ->
            list.mapIndexedNotNull { col, data -> data?.intoTile(row, col) }
        }
    }
}

fun GameState.toEntity() = GameStateEntity(
    state = gridData,
    score = score,
)