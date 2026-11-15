package com.codebythura.fruit2048.data

import com.codebythura.fruit2048.database.GameStateEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random



object GameLogic {

    fun checkIfGameOver(grid: GridMatrix): Boolean {
        for (r in 0 until GRID_SIZE) {
            for (c in 0 until GRID_SIZE) {
                val currentValue = grid[r][c] ?: return false
                if (c + 1 < GRID_SIZE) {
                    val rightValue = grid[r][c + 1]
                    if (rightValue == null || rightValue.value == currentValue.value) return false
                }
                if (r + 1 < GRID_SIZE) {
                    val downValue = grid[r + 1][c]
                    if (downValue == null || downValue.value == currentValue.value) return false
                }
            }
        }
        return true
    }

    fun addRandomData(gridData: GridMatrix, count: Int = 1): GridMatrix {
        if (count <= 0) throw IllegalArgumentException("Count must be greater than 0")
        val emptyCells = gridData.flatMapIndexed { row, list ->
            list.mapIndexedNotNull { col, data -> if (data == null) row to col else null }
        }.shuffled()
        if (emptyCells.isEmpty()) return gridData

        val actualCount = minOf(emptyCells.size, count)
        val newGrid = gridData.map { it.toMutableList() }.toMutableList()

        for (i in 0 until actualCount) {
            val cell = emptyCells[i]
            val value = if (Random.nextFloat() < 0.9f) 2 else 4
            newGrid[cell.first][cell.second] = GridData(value = value)
        }
        return newGrid
    }

}