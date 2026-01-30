package com.codebythura.fruit2048.data

import kotlin.random.Random


const val GRID_SIZE = 4
typealias GridMatrix = Array<Array<GridData?>>

val emptyGrid: GridMatrix = Array(GRID_SIZE) { Array(GRID_SIZE) { null } }

object GameLogic {

    fun addRandomData(gridData: GridMatrix,count: Int = 1) : GridMatrix {
        if (count <= 0) throw IllegalArgumentException("Count must be greater than 0")
        val emptyCells = gridData.flatMapIndexed { row, list ->
            list.mapIndexedNotNull { col, data ->
                if (data == null) row to col else null
            }
        }.shuffled()
        if (emptyCells.isEmpty()) return gridData

        val actualCount = minOf(emptyCells.size,count)
        val newGrid = gridData.map { it.copyOf() }.toTypedArray()

        for (i in 0 until actualCount) {
            val cell = emptyCells[i]
            val value = if (Random.nextFloat() < 0.9f) 2 else 4
            newGrid[cell.first][cell.second] = GridData(value = value)
        }
        return newGrid
    }

    fun swipeDown(gridData: GridMatrix): GridMatrix {
        val rotated90Clockwise = rotate90Clockwise(gridData)
        val swipedToLeft = swipeToLeft(rotated90Clockwise)
        val rotated90AntiClockwise = rotate90AntiClockwise(swipedToLeft)
        return rotated90AntiClockwise
    }

    fun swipeUp(gridData: GridMatrix): GridMatrix {
        val rotated90AntiClockwise = rotate90AntiClockwise(gridData)
        val swipedToLeft = swipeToLeft(rotated90AntiClockwise)
        val rotated90Clockwise = rotate90Clockwise(swipedToLeft)
        return rotated90Clockwise
    }

    fun swipeToRight(gridData: GridMatrix): GridMatrix {
        val reversed = gridData.map { row -> row.reversedArray() }.toTypedArray()
        val swipedToLeft = swipeToLeft(reversed)
        val swipedToRight = swipedToLeft.map { row -> row.reversedArray() }.toTypedArray()
        return swipedToRight
    }

    fun swipeToLeft(gridData: GridMatrix): GridMatrix {
        val swipedToLeft = gridData.map { row ->
            val merged = mergeToLeft(row)
            padToGridSize(merged)
        }.toTypedArray()
        return swipedToLeft
    }

    private fun rotate90AntiClockwise(gridData: GridMatrix): GridMatrix {
        return Array(GRID_SIZE) { row ->
            Array(GRID_SIZE) { col ->
                gridData[col][GRID_SIZE - 1 - row]
            }
        }
    }

    private fun rotate90Clockwise(gridData: GridMatrix): GridMatrix {
        return Array(GRID_SIZE) { row ->
            Array(GRID_SIZE) { col ->
                gridData[GRID_SIZE - 1 - col][row]
            }
        }
    }

    private fun padToGridSize(array: Array<GridData?>): Array<GridData?> {
        val missing = (GRID_SIZE - array.size).coerceAtLeast(0)
        return array + Array(missing) { null }
    }

    private fun mergeToLeft(array: Array<GridData?>): Array<GridData?> {
        val notNulls = array.filterNotNull()
        val merged = mutableListOf<GridData>()
        var i = 0
        while (i < notNulls.size) {
            if (i + 1 < notNulls.size && notNulls[i].value == notNulls[i + 1].value) {
                val data = notNulls[i]
                merged.add(data.copy(value = data.value * 2))
                i += 2
            } else {
                merged.add(notNulls[i])
                i++
            }
        }
        return merged.toTypedArray()
    }

}